from flask import Flask, request, jsonify
from flask_migrate import Migrate
from networkx import Graph
from exts import db
import config  # 导入配置模块
from blueprints.qa import bp as qa_bp
from blueprints.auth import bp as auth_bp
from blueprints.chat import bp as chat_bp
from blueprints.qa_result import bp as qa_result_bp
from blueprints.profile import bp as profile_bp

app = Flask(__name__)

# 加载配置
# 根据不同环境选择配置类
app.config.from_object(config.DevelopmentConfig)  # 你可以替换为 config.ProductionConfig 或 config.TestingConfig

# 初始化数据库
db.init_app(app)

# 初始化迁移管理
migrate = Migrate(app, db)

# 注册蓝图
app.register_blueprint(qa_bp, url_prefix="/qa_bp")
app.register_blueprint(auth_bp, url_prefix="/auth_bp")
app.register_blueprint(chat_bp, url_prefix="/chat_bp")
app.register_blueprint(qa_result_bp, url_prefix="/result_bp")
app.register_blueprint(profile_bp, url_prefix="/profile_bp")

# 检查是否成功加载配置
print(app.config['SQLALCHEMY_DATABASE_URI'])

if __name__ == '__main__':
    print('Starting Flask application...')
    app.run(debug=True, host='192.168.43.37', port=6671)
