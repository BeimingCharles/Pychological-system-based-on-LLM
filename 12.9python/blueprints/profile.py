from flask import Blueprint, jsonify, request, current_app, send_file
from werkzeug.utils import secure_filename
import os
from exts import db
from models import profile
import config
from werkzeug.utils import secure_filename

bp = Blueprint("profile_bp", __name__, url_prefix="/profile")

# 从配置中读取允许上传的图片类型和目标文件夹路径
ALLOWED_EXTENSIONS = config.Config.ALLOWED_EXTENSIONS
UPLOAD_FOLDER = config.Config.UPLOAD_FOLDER


# 判断文件扩展名是否合法
def allowed_file(filename):
    return '.' in filename and filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS

# 确保文件夹存在
if not os.path.exists(UPLOAD_FOLDER):
    os.makedirs(UPLOAD_FOLDER)

@bp.route('/upload', methods=['POST'])
def upload_image():
    if 'profile' not in request.files:
        return jsonify({"error": "图片不存在"}), 400
    
    uploaded_file = request.files['profile']  # 获取上传文件
    user_id = request.form.get('id')  # 获取 id 参数

    # 输出调试信息
    print(f"上传文件: {uploaded_file.filename}")
    print(f"用户 ID: {user_id}")

    if not user_id:
        return jsonify({"error": "用户 ID 缺失"}), 400

    if uploaded_file.filename == '':
        return jsonify({"error": "图片不存在"}), 400

    if uploaded_file and allowed_file(uploaded_file.filename):
        profile_name = secure_filename(uploaded_file.filename)
        profile_path = os.path.join(UPLOAD_FOLDER, profile_name)
        try:
            # 保存图片到文件系统
            uploaded_file.save(profile_path)

            # 检查数据库中是否已有此用户的记录
            existing_profile = profile.query.filter_by(id=user_id).first()
            if existing_profile:
                # 更新已有记录
                existing_profile.profile_location = profile_path
            else:
                # 创建新记录
                new_profile = profile(id=user_id, profile_location=profile_path)
                db.session.add(new_profile)
            
            db.session.commit()

            return jsonify({"message": "图片上传成功", "path": profile_path}), 200

        except Exception as e:
            return jsonify({"error": f"文件保存失败: {str(e)}"}), 500

    return jsonify({"error": "不支持的文件格式"}), 400





@bp.route('/get_profile', methods=['POST'])
def get_image():
    # 获取传递的 id 参数
    user_id = request.get_json().get('id')
    
    if not user_id:
        return jsonify({"error": "缺少用户ID"}), 400

    try:
        # 根据 id 查询用户的图片信息
        data = profile.query.filter_by(id=user_id).first()

        if not data:
            return jsonify({"error": "用户没有上传图片"}), 404

        # 获取图片的文件路径
        profile_path = data.profile_location
        
        # 检查图片文件是否存在
        if not os.path.exists(profile_path):
            return jsonify({"error": "图片文件不存在"}), 404
        
        # 根据文件扩展名确定 mimetype
        _, file_extension = os.path.splitext(profile_path)
        file_extension = file_extension.lower()

        if file_extension == '.jpg' or file_extension == '.jpeg':
            mimetype = 'image/jpeg'
        elif file_extension == '.png':
            mimetype = 'image/png'
        else:
            return jsonify({"error": "不支持的图片格式"}), 400

        # 返回图片文件
        return send_file(profile_path, mimetype=mimetype), 200
        
    except Exception as e:
        return jsonify({"error": f"获取图片失败: {str(e)}"}), 500


