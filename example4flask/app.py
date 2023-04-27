from flask import Flask, request
from flask_sqlalchemy import SQLAlchemy
from sqlalchemy import text

app = Flask(__name__)
HOST = '127.0.0.1'
USERNAME = 'root'
PASSWORD = '871403165'
DATABASE = 'test'
PORT = 3306

# 连接数据库
app.config['SQLALCHEMY_DATABASE_URI'] = f"mysql+pymysql://{USERNAME}:{PASSWORD}@{HOST}:{PORT}/{DATABASE}?charset=utf8"
db = SQLAlchemy(app)

# 执行sql
with app.app_context():
    with db.engine.connect() as conn:
        rs = conn.execute(text("select 1"))
        print(rs.fetchone())

@app.route('/')
def hello_world():
    return 'Hello World!'


# 结构路径上的参数
# 可以使用的类型有string, int, float, path, uuid, any(类似枚举)
@app.route("/path/variable/<int:id>")
def path_variable(id):
    return "您访问的博客id是: %s" % id

# 类似于枚举, 只能接收指定的参数
@app.route("/path/category/<any(python, flask, django):category>")
def path_category(category):
    return category

# 从query string中获取参数
def query_string():
    page = request.args.get("page", default=1, type=int)
    return f"您获取的是第{page}页的图书"


if __name__ == '__main__':
    # debug: 开启后可以进行代码热部署, ctrl+s保存后即可, 也可以通过--debug=True开启或者直接勾选启动配置中的FLASK_DEBUG
    # host: 修改绑定的host, 使得其他机器可以访问, 也可以使用--host=0.0.0.0
    # port: 默认为5000, 也可以使用--port=8888
    # 上面的host和port在pycharm中, 如果是使用flask server的方式启动的话会无效(2018以上版本有问题)
    # 点击运行按钮的Edit Configurations
    # 使用python启动方式即可(https://blog.csdn.net/JENREY/article/details/86699817)
    app.run(debug=True, host="0.0.0.0", port=8888)
