package com.tiger.hive.udf;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.exec.UDFArgumentLengthException;
import org.apache.hadoop.hive.ql.exec.vector.VectorizedExpressions;
import org.apache.hadoop.hive.ql.exec.vector.expressions.StringLower;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDFUtils;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector.Category;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector.PrimitiveCategory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorConverter;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.typeinfo.BaseCharTypeInfo;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfoFactory;

// 使用在show function的时候
@Description(name = "lower,lcase",
        value = "_FUNC_(str) - Returns str with all characters changed to lowercase",
        extended = "Example:\n"
                + "  > SELECT _FUNC_('Facebook') FROM src LIMIT 1;\n" + "  'facebook'")
@VectorizedExpressions({StringLower.class})
public class GenericUDFLowerExample extends GenericUDF {
    // 用以保存调用lower函数的入参的类型
    private transient PrimitiveObjectInspector argumentOI;
    private transient PrimitiveObjectInspectorConverter.StringConverter stringConverter;
    private transient PrimitiveCategory returnType = PrimitiveCategory.STRING;
    private transient GenericUDFUtils.StringHelper returnHelper;

    /**
     * 传入调用lower是传入的参数类型
     * 返回lower返回的参数类型
     */
    @Override
    public ObjectInspector initialize(ObjectInspector[] arguments) throws UDFArgumentException {
        // 对传入的参数个数进行解析
        if (arguments.length != 1) {
            throw new UDFArgumentLengthException(
                    "LOWER requires 1 argument, got " + arguments.length);
        }
        // 对传入的参数进行类型判断
        if (arguments[0].getCategory() != Category.PRIMITIVE) {
            throw new UDFArgumentException(
                    "LOWER only takes primitive types, got " + argumentOI.getTypeName());
        }
        argumentOI = (PrimitiveObjectInspector) arguments[0];

        stringConverter = new PrimitiveObjectInspectorConverter.StringConverter(argumentOI);
        PrimitiveCategory inputType = argumentOI.getPrimitiveCategory();
        ObjectInspector outputOI = null;
        BaseCharTypeInfo typeInfo;
        // 判断输入的类型是char还是varchar, 或者是string
        switch (inputType) {
            case CHAR:
                // return type should have same length as the input.
                returnType = inputType;
                typeInfo = TypeInfoFactory.getCharTypeInfo(
                        GenericUDFUtils.StringHelper.getFixedStringSizeForType(argumentOI));
                outputOI = PrimitiveObjectInspectorFactory.getPrimitiveWritableObjectInspector(typeInfo);
                break;
            case VARCHAR:
                // return type should have same length as the input.
                returnType = inputType;
                typeInfo = TypeInfoFactory.getVarcharTypeInfo(
                        GenericUDFUtils.StringHelper.getFixedStringSizeForType(argumentOI));
                outputOI = PrimitiveObjectInspectorFactory.getPrimitiveWritableObjectInspector(typeInfo);
                break;
            default:
                returnType = PrimitiveCategory.STRING;
                outputOI = PrimitiveObjectInspectorFactory.writableStringObjectInspector;
                break;
        }
        returnHelper = new GenericUDFUtils.StringHelper(returnType);
        return outputOI;
    }

    /**
     * 调用evaluate来触发函数的计算
     * 传入的对象就是调用lower时入参的值的代理对象
     */
    @Override
    public Object evaluate(DeferredObject[] arguments) throws HiveException {
        String val = null;
        if (arguments[0] != null) {
            // 必须调用DeferredObject对象的get方法来获取传入的参数的值
            val = (String) stringConverter.convert(arguments[0].get());
        }
        if (val == null) {
            return null;
        }
        val = val.toLowerCase();
        return returnHelper.setReturnValue(val);
    }

    @Override
    public String getDisplayString(String[] children) {
        return getStandardDisplayString("lower", children);
    }

}
