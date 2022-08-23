package com.tiger.hive.udf;


import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDFArgumentTypeException;
import org.apache.hadoop.hive.ql.exec.vector.VectorizedUDAFs;
import org.apache.hadoop.hive.ql.exec.vector.expressions.aggregates.gen.*;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.parse.SemanticException;
import org.apache.hadoop.hive.ql.plan.ptf.WindowFrameDef;
import org.apache.hadoop.hive.ql.udf.UDFType;
import org.apache.hadoop.hive.ql.udf.generic.AbstractGenericUDAFResolver;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDAFEvaluator;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDAFMax;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDAFStreamingEvaluator;
import org.apache.hadoop.hive.ql.util.JavaDataModel;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorUtils;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorUtils.ObjectInspectorCopyOption;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfo;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Description(name = "max", value = "_FUNC_(expr) - Returns the maximum value of expr")
public class GenericUDAFMaxExample extends AbstractGenericUDAFResolver {

    static final Logger LOG = LoggerFactory.getLogger(GenericUDAFMax.class.getName());

    // 传入调用udaf的入参的类型, 可以进行参数个数和类型的校验
    // 返回一个聚合的Evalutor
    @Override
    public GenericUDAFEvaluator getEvaluator(TypeInfo[] parameters)
            throws SemanticException {
        if (parameters.length != 1) {
            throw new UDFArgumentTypeException(parameters.length - 1,
                    "Exactly one argument is expected.");
        }
        ObjectInspector oi = TypeInfoUtils.getStandardJavaObjectInspectorFromTypeInfo(parameters[0]);
        if (!ObjectInspectorUtils.compareSupported(oi)) {
            throw new UDFArgumentTypeException(parameters.length - 1,
                    "Cannot support comparison of map<> type or complex type containing map<>.");
        }
        return new GenericUDAFMaxEvaluator();
    }

    // 用来进行聚合的Evaluator, 下面两个注解不知道有什么作用
    @UDFType(distinctLike=true)
    @VectorizedUDAFs({
            VectorUDAFMaxLong.class,
            VectorUDAFMaxDouble.class,
            VectorUDAFMaxDecimal.class,
            VectorUDAFMaxDecimal64.class,
            VectorUDAFMaxTimestamp.class,
            VectorUDAFMaxIntervalDayTime.class,
            VectorUDAFMaxString.class})
    public static class GenericUDAFMaxEvaluator extends GenericUDAFEvaluator {

        private transient ObjectInspector inputOI;
        private transient ObjectInspector outputOI;

        // 传入调用udaf的入参的类型, 做一个初始化
        @Override
        public ObjectInspector init(Mode m, ObjectInspector[] parameters)
                throws HiveException {
            assert (parameters.length == 1);
            super.init(m, parameters);
            inputOI = parameters[0];
            // Copy to Java object because that saves object creation time.
            // Note that on average the number of copies is log(N) so that's not
            // very important.
            outputOI = ObjectInspectorUtils.getStandardObjectInspector(inputOI,
                    ObjectInspectorCopyOption.JAVA);
            return outputOI;
        }

        // 一个聚合器, 用来保存多行直接聚合的中间状态, 这里是保存聚合时的最大值
        // 下面注解不知道有什么作用
        @AggregationType(estimable = true)
        static class MaxAgg extends AbstractAggregationBuffer {
            Object o;
            @Override
            public int estimate() {
                return JavaDataModel.PRIMITIVES2;
            }
        }

        // 返回一个新的聚合器
        @Override
        public AggregationBuffer getNewAggregationBuffer() throws HiveException {
            MaxAgg result = new MaxAgg();
            return result;
        }

        // 重置聚合器
        @Override
        public void reset(AggregationBuffer agg) throws HiveException {
            MaxAgg myagg = (MaxAgg) agg;
            myagg.o = null;
        }

        boolean warned = false;

        // 传入的是聚合器和传入udaf的用于聚合的数据
        @Override
        public void iterate(AggregationBuffer agg, Object[] parameters)
                throws HiveException {
            assert (parameters.length == 1);
            merge(agg, parameters[0]);
        }

        // 这里应该是分区内聚合完成的时候调用, 返回一个分区内聚合的最终结果
        @Override
        public Object terminatePartial(AggregationBuffer agg) throws HiveException {
            return terminate(agg);
        }

        // 这里应该是分区间进行聚合
        @Override
        public void merge(AggregationBuffer agg, Object partial)
                throws HiveException {
            if (partial != null) {
                MaxAgg myagg = (MaxAgg) agg;
                int r = ObjectInspectorUtils.compare(myagg.o, outputOI, partial, inputOI);
                if (myagg.o == null || r < 0) {
                    myagg.o = ObjectInspectorUtils.copyToStandardObject(partial, inputOI,
                            ObjectInspectorCopyOption.JAVA);
                }
            }
        }

        // 这里是分区间聚合完成时调用, 返回一个分区间聚合的最终结果
        @Override
        public Object terminate(AggregationBuffer agg) throws HiveException {
            MaxAgg myagg = (MaxAgg) agg;
            return myagg.o;
        }

        // 返回一个开窗的聚合器, 应该是在开窗函数的时候使用udtf时调用
        @Override
        public GenericUDAFEvaluator getWindowingEvaluator(WindowFrameDef wFrmDef) {
            return new MaxStreamingFixedWindow(this, wFrmDef);
        }

    }
    static class MaxStreamingFixedWindow extends
            GenericUDAFStreamingEvaluator<Object> {


        public MaxStreamingFixedWindow(GenericUDAFEvaluator wrappedEval,
                                       WindowFrameDef wFrmDef) {
            super(wrappedEval, wFrmDef);
        }

        @Override
        public AggregationBuffer getNewAggregationBuffer() throws HiveException {
            return null;
        }

        @Override
        public void iterate(AggregationBuffer agg, Object[] parameters)
                throws HiveException {

        }

        @Override
        public Object terminate(AggregationBuffer agg) throws HiveException {
            return null;
        }

        @Override
        public int getRowsRemainingAfterTerminate() throws HiveException {
            return 0;
        }

    }

}
