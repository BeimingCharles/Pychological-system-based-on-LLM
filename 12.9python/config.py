import os

# 基础配置
class Config:
    SECRET_KEY = os.urandom(24)  # 用于会话签名等，需要保密
    DEBUG = True  # 开发模式
    TESTING = False  # 测试模式

    # 数据库配置
    SQLALCHEMY_TRACK_MODIFICATIONS = False  # 防止 Flask 跟踪数据库的修改

    # 其他配置
    UPLOAD_FOLDER = 'D:/test'  # Windows D 盘下的 test 文件夹作为图片保存路径
    ALLOWED_EXTENSIONS = {'jpg', 'jpeg', 'png'}  # 确保这里包含正确的扩展名
  # 允许上传的图片类型


# 数据库连接配置
class DatabaseConfig(Config):
    HOSTNAME = os.environ.get('DB_HOST', '127.0.0.1')  # 数据库主机地址
    PORT = os.environ.get('DB_PORT', '3306')  # 数据库端口
    DATABASE = os.environ.get('DB_NAME', 'test_db')  # 数据库名称
    USERNAME = os.environ.get('DB_USER', 'root')  # 数据库用户名
    PASSWORD = os.environ.get('DB_PASSWORD', 'your_password_here')  # 数据库密码

    # 动态构建数据库连接字符串
    SQLALCHEMY_DATABASE_URI = "mysql+pymysql://{}:{}@{}:{}/{}?charset=utf8".format(
        USERNAME, PASSWORD, HOSTNAME, PORT, DATABASE
    )


# 环境配置（你可以根据不同的环境创建不同的配置类）
class DevelopmentConfig(DatabaseConfig):
    DEBUG = True
    # SQLALCHEMY_DATABASE_URI 会继承 DatabaseConfig 中的动态构建

class ProductionConfig(DatabaseConfig):
    DEBUG = False
    # SQLALCHEMY_DATABASE_URI 会继承 DatabaseConfig 中的动态构建

class TestingConfig(DatabaseConfig):
    TESTING = True
    # SQLALCHEMY_DATABASE_URI 会继承 DatabaseConfig 中的动态构建
