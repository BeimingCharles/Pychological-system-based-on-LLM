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
    HOSTNAME = '47.121.189.213'  # 数据库主机地址
    PORT = '3306'  # 数据库端口
    DATABASE = 'cstest'  # 数据库名称
    USERNAME = 'CStest'  # 数据库用户名
    PASSWORD = 'LfD2YfYnkAn7NaLd'  # 数据库密码

    # 动态构建数据库连接字符串
    SQLALCHEMY_DATABASE_URI = "mysql+pymysql://{}:{}@{}:{}/{}?charset=utf8".format(
        USERNAME, PASSWORD, HOSTNAME, PORT, DATABASE
    )


# 环境配置（你可以根据不同的环境创建不同的配置类）
class DevelopmentConfig(DatabaseConfig):
    DEBUG = True
    SQLALCHEMY_DATABASE_URI = "mysql+pymysql://CStest:LfD2YfYnkAn7NaLd@47.121.189.213:3306/cstest?charset=utf8"  # 开发环境数据库

class ProductionConfig(DatabaseConfig):
    DEBUG = False
    SQLALCHEMY_DATABASE_URI = "mysql+pymysql://CStest:LfD2YfYnkAn7NaLd@47.121.189.213:3306/cstest?charset=utf8"  # 生产环境数据库

class TestingConfig(DatabaseConfig):
    TESTING = True
    SQLALCHEMY_DATABASE_URI = "mysql+pymysql://CStest:LfD2YfYnkAn7NaLd@47.121.189.213:3306/cstest?charset=utf8"  # 测试环境数据库
