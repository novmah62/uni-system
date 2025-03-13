# post_spam.py

import pandas as pd
import re
from sklearn.model_selection import train_test_split
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.linear_model import LogisticRegression
from sklearn.metrics import classification_report, confusion_matrix
import joblib

def preprocess_text(text):
    # Nếu không phải chuỗi, chuyển về chuỗi hoặc trả về chuỗi rỗng
    if not isinstance(text, str):
        text = str(text) if pd.notnull(text) else ""
    text = text.lower()
    text = re.sub(r'[^a-z0-9\s]', '', text)
    text = re.sub(r'\s+', ' ', text).strip()
    return text

def main():
    # Đường dẫn tới file post spam CSV
    post_file = '/Users/ManhAT18A/Desktop/ML/data/post_spam.csv'
    df_post = pd.read_csv(post_file)
    print("Post Spam Dataset Columns:", df_post.columns.tolist())
    
    # Xây dựng DataFrame với 2 cột: text và label
    df = pd.DataFrame({
        'text': df_post['texts_vi'],
        'label': df_post['labels']
    })
    
    # Tiền xử lý văn bản
    df['text_clean'] = df['text'].apply(preprocess_text)
    print("Sample processed text:\n", df[['text', 'text_clean', 'label']].head())
    
    # Chuẩn bị dữ liệu huấn luyện
    X = df['text_clean']
    y = df['label']
    
    X_train, X_test, y_train, y_test = train_test_split(
        X, y, test_size=0.2, random_state=42, stratify=y
    )
    
    # Vector hóa văn bản sử dụng TF-IDF
    vectorizer = TfidfVectorizer(stop_words='english', max_features=5000)
    X_train_vec = vectorizer.fit_transform(X_train)
    X_test_vec = vectorizer.transform(X_test)
    
    # Huấn luyện mô hình Logistic Regression
    model = LogisticRegression(max_iter=1000)
    model.fit(X_train_vec, y_train)
    
    # Đánh giá mô hình
    y_pred = model.predict(X_test_vec)
    print("Confusion Matrix:")
    print(confusion_matrix(y_test, y_pred))
    print("\nClassification Report:")
    print(classification_report(y_test, y_pred))
    
    # Lưu mô hình và vectorizer
    joblib.dump(model, 'post_spam_model.pkl')
    joblib.dump(vectorizer, 'post_tfidf_vectorizer.pkl')
    print("Post spam model and vectorizer saved.")

if __name__ == '__main__':
    main()
