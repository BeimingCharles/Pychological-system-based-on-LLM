from exts import db
from datetime import datetime


class questionnaire_index(db.Model):
    __tablename__="questionnaire_index"
    id = db.Column(db.Integer, primary_key=True, autoincrement=True)
    questionnaire_name = db.Column(db.String(100), nullable=False)
    questionnaire_introduction = db.Column(db.String(100), nullable=False)
    questionnaire_num = db.Column(db.String(100), nullable=False)
    q_table_name = db.Column(db.String(100), nullable=False)
    __table_args__ = {'extend_existing': True}

class scl_90(db.Model):
    __tablename__ = "scl_90"
    id = db.Column(db.Integer, primary_key=True, autoincrement=True)
    question= db.Column(db.String(100), nullable=False)
    __table_args__ = {'extend_existing': True}
    
class SDS(db.Model):
    __tablename__ = "SDS"
    id = db.Column(db.Integer, primary_key=True, autoincrement=True)
    question= db.Column(db.String(100), nullable=False)
    __table_args__ = {'extend_existing': True}

class User(db.Model):
    __tablename__ = "user"
    id = db.Column(db.Integer, primary_key=True, autoincrement=True)
    username = db.Column(db.String(100), nullable=False)
    password = db.Column(db.String(100), nullable=False)
    scl_90 = db.Column(db.String(100), nullable=True)
    SDS = db.Column(db.String(100), nullable=True)
    __table_args__ = {'extend_existing': True}

class profile(db.Model):
    __tablename__ = "profile"
    id = db.Column(db.Integer, primary_key=True, autoincrement=True)
    username = db.Column(db.String(100), nullable=False)
    profile_location = db.Column(db.String(100), nullable=True)
    __table_args__ = {'extend_existing': True}