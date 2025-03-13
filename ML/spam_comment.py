



import json
import re
import joblib
from kafka import KafkaConsumer, KafkaProducer

# Cấu hình Kafka
KAFKA_BROKER = 'localhost:29092'
TOPIC_NAME_RECIVE = 'comments'
TOPIC_NAME_SEND = 'spam-comments'

# Hàm deserializer chuyển byte về JSON (với unescaping)
def json_deserializer(x):
    try:
        decoded = x.decode('utf-8')
    except Exception as e:
        print("Error decoding message:", e)
        return None
    # Loại bỏ ký tự null và khoảng trắng
    cleaned = decoded.replace('\x00', '').strip()
    if not cleaned:
        return None
    # Nếu chuỗi được bao quanh bởi dấu ngoặc kép, loại bỏ chúng
    if cleaned[0] == '"' and cleaned[-1] == '"':
        cleaned = cleaned[1:-1]
    # Nếu chuỗi chứa ký tự escape, unescape chúng
    if '\\"' in cleaned:
        try:
            cleaned = cleaned.encode('utf-8').decode('unicode_escape')
        except Exception as e:
            print("Error unescaping cleaned string:", e, "for message:", cleaned)
            return None
    # Nếu chuỗi bắt đầu bằng { hoặc [, cố gắng parse JSON
    if cleaned and cleaned[0] in ['{', '[']:
        try:
            return json.loads(cleaned)
        except Exception as e:
            print("Error parsing JSON:", e, "for message:", cleaned)
            return None
    else:
        return cleaned

# Cấu hình Kafka Consumer để nhận message
consumer = KafkaConsumer(
    TOPIC_NAME_RECIVE,
    bootstrap_servers=[KAFKA_BROKER],
    auto_offset_reset='earliest',
    group_id='python-consumer-group',
    value_deserializer=json_deserializer
)

# Cấu hình Kafka Producer để gửi message đã xử lý
producer = KafkaProducer(
    bootstrap_servers=[KAFKA_BROKER],
    value_serializer=lambda x: json.dumps(x).encode('utf-8')
)

# Định nghĩa lớp Comment
class Comment:
    def __init__(self, commentId, content, label):
        self.commentId = commentId
        self.content = content
        self.label = label

    def __repr__(self):
        return f"Comment(commentId={self.commentId}, content={self.content}, label={self.label})"

# Hàm chuyển đổi dict thành đối tượng Comment
def dict_to_comment(d):
    if isinstance(d, dict):
        return Comment(
            commentId=d.get('commentId'),
            content=d.get('content'),
            label=d.get('label')
        )
    return None

# Hàm tiền xử lý văn bản
def preprocess_text(text):
    if not isinstance(text, str):
        text = str(text) if text is not None else ""
    text = text.lower()
    text = re.sub(r'[^a-z0-9\s]', '', text)
    text = re.sub(r'\s+', ' ', text).strip()
    return text

# Load mô hình và vectorizer đã huấn luyện cho comment spam
comment_model = joblib.load('comment_spam_model.pkl')
comment_vectorizer = joblib.load('comment_tfidf_vectorizer.pkl')

# Hàm xử lý comment: tiền xử lý, vector hóa, dự đoán nhãn và gửi kết quả
def produce(comment):
    # Tiền xử lý nội dung comment
    preprocessed = preprocess_text(comment.content)
    # Vector hóa nội dung
    vectorized = comment_vectorizer.transform([preprocessed])
    # Dự đoán nhãn (ví dụ: 'spam' hoặc 'ham')
    prediction = comment_model.predict(vectorized)
    # Cập nhật nhãn cho comment
    comment.label = prediction[0]
    # Chuyển đối tượng Comment thành dict
    comment_dict = {
        "commentId": comment.commentId,
        "content": comment.content,
        "label": comment.label
    }
    # Gửi comment đã xử lý vào topic spam-comments
    producer.send(TOPIC_NAME_SEND, comment_dict)
    producer.flush()
    print("Processed comment sent:", comment_dict)

# Hàm lắng nghe message từ Kafka, xử lý và gửi kết quả
def consume_messages():
    print("Starting to consume messages...")
    for msg in consumer:
        value = msg.value
        if value is not None:
            print("Received message dict:", value)
            comment_obj = dict_to_comment(value)
            print("Converted to Comment object:", comment_obj)
            if comment_obj is not None:
                produce(comment_obj)
            else:
                print("Failed to convert dict to Comment object.")
        else:
            print("Received empty or invalid message.")

if __name__ == '__main__':
    consume_messages()
