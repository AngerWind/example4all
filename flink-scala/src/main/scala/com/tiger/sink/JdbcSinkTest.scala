package com.tiger.sink

import java.sql.{Connection, DriverManager, PreparedStatement}

import com.tiger.SensorReading
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.sink.{RichSinkFunction, SinkFunction}
import org.apache.flink.streaming.api.scala._


/**
 * @author Tiger.Shen
 * @date 2020/12/27 18:14
 */
object JdbcSinkTest {

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    // 从文件中读取
    val inputPath = "src\\main\\resources\\sensor.txt"
    val stream2 = env.readTextFile(inputPath)

    val dataStream = stream2
      .map(data => {
        val arr = data.split(",")
        SensorReading(arr(0), arr(1).toLong, arr(2).toDouble)
      })


    env.execute()

  }
}

class MyJdbcSinkFunction() extends RichSinkFunction[SensorReading]{

  var conn: Connection = _
  var insertStmt: PreparedStatement = _
  var updateStmt: PreparedStatement = _

  override def invoke(value: SensorReading, context: SinkFunction.Context[_]): Unit = {
    // 先执行更新操作，查到就更新
    updateStmt.setDouble(1, value.temperature)
    updateStmt.setString(2, value.id)
    updateStmt.execute()
    // 如果更新没有查到数据，那么就插入
    if( updateStmt.getUpdateCount == 0 ){
      insertStmt.setString(1, value.id)
      insertStmt.setDouble(2, value.temperature)
      insertStmt.execute()
    }
  }

  override def close(): Unit = {
    insertStmt.close()
    updateStmt.close()
    conn.close()
  }

  override def open(parameters: Configuration): Unit = {
    conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "123456")
    insertStmt = conn.prepareStatement("insert into sensor_temp (id, temp) values (?, ?)")
    updateStmt = conn.prepareStatement("update sensor_temp set temp = ? where id = ?")
  }
}
