

# import pandas as pd
# import re
# import nltk
# from nltk.corpus import stopwords
# from nltk.stem import WordNetLemmatizer
# from sklearn.feature_extraction.text import TfidfVectorizer
# from sklearn.model_selection import train_test_split
# from sklearn.linear_model import LogisticRegression
# from sklearn.metrics import accuracy_score, confusion_matrix
# import joblib

# # Tải các tài nguyên cần thiết của NLTK
# nltk.download('stopwords')
# nltk.download('wordnet')

# # Đọc dữ liệu từ file CSV
# df = pd.read_csv('/Users/ManhAT18A/ML/data/Youtube01.csv')

# # Kiểm tra dữ liệu đầu vào
# print(df.head())

# # Tiền xử lý dữ liệu văn bản
# lemmatizer = WordNetLemmatizer()

# # Hàm xử lý văn bản
# def preprocess_text(text):
#     # Chuyển về chữ thường
#     text = text.lower()
    
#     # Xóa bỏ các ký tự không phải chữ
#     text = re.sub(r'[^a-z\s]', '', text)
    
#     # Tách từ và lemmatize
#     words = text.split()
#     words = [lemmatizer.lemmatize(word) for word in words if word not in stopwords.words('english')]
    
#     return ' '.join(words)

# # Áp dụng tiền xử lý cho cột 'CONTENT'
# df['CONTENT'] = df['CONTENT'].apply(preprocess_text)

# # Kiểm tra lại dữ liệu sau khi tiền xử lý
# print(df.head())

# # Vector hóa dữ liệu văn bản sử dụng TF-IDF
# vectorizer = TfidfVectorizer(max_features=5000)
# X = vectorizer.fit_transform(df['CONTENT']).toarray()

# # Chia dữ liệu thành tập huấn luyện và kiểm tra (80% huấn luyện, 20% kiểm tra)
# X_train, X_test, y_train, y_test = train_test_split(X, df['CLASS'], test_size=0.2, random_state=42)

# # Huấn luyện mô hình Logistic Regression
# model = LogisticRegression()
# model.fit(X_train, y_train)

# # Dự đoán trên tập kiểm tra
# y_pred = model.predict(X_test)

# # Đánh giá mô hình
# accuracy = accuracy_score(y_test, y_pred)
# conf_matrix = confusion_matrix(y_test, y_pred)

# # In ra độ chính xác và confusion matrix
# print(f'Accuracy: {accuracy*100:.2f}%')
# print(f'Confusion Matrix: \n{conf_matrix}')

# # Lưu mô hình vào file
# joblib.dump(model, 'comment_classification_model.pkl')
# joblib.dump(vectorizer, 'tfidf_vectorizer.pkl')

# # Dự đoán cho một bình luận mới
# new_comment = "Nice try bro :3"
# new_comment_processed = preprocess_text(new_comment)
# new_comment_vectorized = vectorizer.transform([new_comment_processed]).toarray()

# # Dự đoán lớp của bình luận mới
# prediction = model.predict(new_comment_vectorized)
# print(f"Predicted class: {prediction[0]}")





# ------------


# import pickle

# # Đảm bảo đường dẫn file chính xác
# model_path = '/Users/ManhAT18A/ML/comment_classification_model.pkl'
# vectorizer_path = '/Users/ManhAT18A/ML/tfidf_vectorizer.pkl'

# # Tải lại mô hình và vectorizer từ file .pkl
# try:
#     with open(model_path, 'rb') as model_file:
#         model = pickle.load(model_file)
#     with open(vectorizer_path, 'rb') as vectorizer_file:
#         vectorizer = pickle.load(vectorizer_file)
#     print("Mô hình và vectorizer đã được tải thành công.")
# except FileNotFoundError as e:
#     print(f"Lỗi: {e}. Hãy kiểm tra lại đường dẫn file.")
# except pickle.UnpicklingError as e:
#     print(f"Lỗi khi giải mã file pickle: {e}. Kiểm tra lại xem file pickle có bị hỏng không.")
# except Exception as e:
#     print(f"Đã xảy ra lỗi trong quá trình tải file: {e}")

# # Kiểm tra nếu model và vectorizer đã được tải
# if 'model' not in locals() or 'vectorizer' not in locals():
#     print("Không thể tải mô hình và vectorizer. Kiểm tra lại các file.")
# else:
#     # Dự đoán cho một bình luận mẫu
#     comment = "Check out my new channel!"
#     print(f"Comment: {comment}")

#     # Chuyển bình luận thành vector sử dụng vectorizer
#     comment_vector = vectorizer.transform([comment])

#     # Dự đoán xem bình luận là spam hay không
#     prediction = model.predict(comment_vector)

#     # In kết quả dự đoán
#     if prediction[0] == 1:
#         print("Dự đoán: Bình luận này là Spam.")
#     else:
#         print("Dự đoán: Bình luận này không phải Spam.")






# import pandas as pd
# import re
# import nltk
# from nltk.corpus import stopwords
# from nltk.stem import WordNetLemmatizer
# from sklearn.feature_extraction.text import TfidfVectorizer
# from sklearn.model_selection import train_test_split
# from sklearn.linear_model import LogisticRegression
# from sklearn.metrics import accuracy_score, confusion_matrix
# import joblib

# # Tải các tài nguyên cần thiết của NLTK
# nltk.download('stopwords')
# nltk.download('wordnet')

# # Đọc dữ liệu từ file CSV
# df = pd.read_csv('/Users/ManhAT18A/Desktop/ML/data/Youtube01.csv')

# # Kiểm tra dữ liệu đầu vào
# print(df.head())

# # Tiền xử lý dữ liệu văn bản
# lemmatizer = WordNetLemmatizer()

# # Hàm xử lý văn bản
# def preprocess_text(text):
#     # Chuyển về chữ thường
#     text = text.lower()
    
#     # Xóa bỏ các ký tự không phải chữ
#     text = re.sub(r'[^a-z\s]', '', text)
    
#     # Tách từ và lemmatize
#     words = text.split()
#     words = [lemmatizer.lemmatize(word) for word in words if word not in stopwords.words('english')]
    
#     return ' '.join(words)

# # Áp dụng tiền xử lý cho cột 'CONTENT'
# df['CONTENT'] = df['CONTENT'].apply(preprocess_text)

# # Kiểm tra lại dữ liệu sau khi tiền xử lý
# print(df.head())

# # Vector hóa dữ liệu văn bản sử dụng TF-IDF
# vectorizer = TfidfVectorizer(max_features=5000)
# X = vectorizer.fit_transform(df['CONTENT']).toarray()

# # Chia dữ liệu thành tập huấn luyện và kiểm tra (80% huấn luyện, 20% kiểm tra)
# X_train, X_test, y_train, y_test = train_test_split(X, df['CLASS'], test_size=0.2, random_state=42)

# # Huấn luyện mô hình Logistic Regression
# model = LogisticRegression()
# model.fit(X_train, y_train)

# # Dự đoán trên tập kiểm tra
# y_pred = model.predict(X_test)

# # Đánh giá mô hình
# accuracy = accuracy_score(y_test, y_pred)
# conf_matrix = confusion_matrix(y_test, y_pred)

# # In ra độ chính xác và confusion matrix
# print(f'Accuracy: {accuracy*100:.2f}%')
# print(f'Confusion Matrix: \n{conf_matrix}')

# # Lưu mô hình vào file bằng joblib
# joblib.dump(model, '/Users/ManhAT18A/Desktop/ML/comment_classification_model.pkl')
# joblib.dump(vectorizer, '/Users/ManhAT18A/Desktop/ML/tfidf_vectorizer.pkl')

# # Dự đoán cho một bình luận mới
# new_comment = "Nice try bro :3"
# new_comment_processed = preprocess_text(new_comment)
# new_comment_vectorized = vectorizer.transform([new_comment_processed]).toarray()

# # Dự đoán lớp của bình luận mới
# prediction = model.predict(new_comment_vectorized)
# print(f"Predicted class: {prediction[0]}")





# import joblib

# # Kafka -> 
# # Tải lại mô hình và vectorizer từ file .pkl
# try:
#     model = joblib.load('/Users/ManhAT18A/Desktop/ML/comment_classification_model.pkl')
#     vectorizer = joblib.load('/Users/ManhAT18A/Desktop/ML/tfidf_vectorizer.pkl')
#     print("Mô hình và vectorizer đã được tải thành công.")
# except Exception as e:
#     print(f"Đã xảy ra lỗi trong quá trình tải file: {e}")

# # Dự đoán cho một bình luận mẫu
# comment = "Visit my new website! Check out all the cool stuff, click here now: www.spamwebsite.com!!!"
# # comment = "nice job :3 sub now"

# print(f"Comment: {comment}")

# # Tiền xử lý bình luận mới
# def preprocess_text(text):
#     import re
#     from nltk.corpus import stopwords
#     from nltk.stem import WordNetLemmatizer
    
#     lemmatizer = WordNetLemmatizer()
    
#     # Chuyển về chữ thường
#     text = text.lower()
    
#     # Xóa bỏ các ký tự không phải chữ
#     text = re.sub(r'[^a-z\s]', '', text)
    
#     # Tách từ và lemmatize
#     words = text.split()
#     words = [lemmatizer.lemmatize(word) for word in words if word not in stopwords.words('english')]
    
#     return ' '.join(words)

# # Chuyển bình luận thành vector sử dụng vectorizer
# comment_processed = preprocess_text(comment)
# comment_vector = vectorizer.transform([comment_processed])

# # Dự đoán xem bình luận là spam hay không
# prediction = model.predict(comment_vector)

# # In kết quả dự đoán
# if prediction[0] == 1:
#     print("Dự đoán: Bình luận này là Spam.")
# else:
#     print("Dự đoán: Bình luận này không phải Spam.")



import json
import time
from kafka import KafkaConsumer, KafkaProducer

# Địa chỉ Kafka broker: thay đổi theo môi trường chạy của bạn.
KAFKA_BROKER = 'localhost:29092'  # Nếu chạy từ host; nếu chạy trong container, có thể là 'kafka:9092'
TOPIC_NAME = 'comments'

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

    # Nếu message có vẻ là JSON (bắt đầu bằng { hoặc [), cố gắng parse JSON
    if cleaned[0] in ['{', '[']:
        try:
            return json.loads(cleaned)
        except Exception as e:
            print("Error parsing JSON:", e, "for message:", cleaned)
            return None
    else:
        # Nếu không, trả về raw string
        return cleaned

# Khởi tạo Kafka Producer
producer = KafkaProducer(
    bootstrap_servers=[KAFKA_BROKER],
    value_serializer=lambda x: json.dumps(x).encode('utf-8')
)

# Khởi tạo Kafka Consumer với hàm deserializer tùy chỉnh
consumer = KafkaConsumer(
    TOPIC_NAME,
    bootstrap_servers=[KAFKA_BROKER],
    auto_offset_reset='earliest',
    group_id='python-consumer-group',
    value_deserializer=json_deserializer
)

def produce_message():
    """Hàm gửi một message mẫu ở định dạng JSON tới Kafka topic."""
    message = {
        "commentId": "Hello from Python producer!",
        "content": "time.time()",
        "label" : "label"
    }
    producer.send(TOPIC_NAME, message)
    producer.flush()  # Đảm bảo message được gửi ngay lập tức
    print("Produced message:", message)

def consume_messages():
    """Hàm lắng nghe và in ra các message nhận được từ Kafka topic."""
    print("Starting to consume messages...")
    for msg in consumer:
        value = msg.value
        if value is not None:
            print("Received message:", value)
        else:
            print("Received empty or invalid message.")

if __name__ == '__main__':
    # Gửi một message đầu tiên để kiểm tra
    produce_message()
    # Bắt đầu lắng nghe message liên tục
    consume_messages()






# import pandas as pd

# # Đường dẫn tới file comment spam
# comment_file = '/Users/ManhAT18A/Desktop/ML/data/comment_spam.csv'
# comment_df = pd.read_csv(comment_file)
# print("Comment Spam Dataset:")
# print(comment_df.head())             # Hiển thị 5 dòng đầu
# print("Columns:", comment_df.columns.tolist())

# # Đường dẫn tới file post spam
# post_file = '/Users/ManhAT18A/Desktop/ML/data/post_spam.csv'
# post_df = pd.read_csv(post_file)
# print("\nPost Spam Dataset:")
# print(post_df.head())                # Hiển thị 5 dòng đầu
# print("Columns:", post_df.columns.tolist())
