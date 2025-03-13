import joblib
import re

def preprocess_text(text):
    # Hàm tiền xử lý tương tự như khi bạn huấn luyện mô hình
    if not isinstance(text, str):
        text = str(text) if text is not None else ""
    text = text.lower()
    text = re.sub(r'[^a-z0-9\s]', '', text)
    text = re.sub(r'\s+', ' ', text).strip()
    return text

# --- Đối với Comment Spam ---
# Load mô hình và vectorizer cho comment spam
comment_model = joblib.load('comment_spam_model.pkl')
comment_vectorizer = joblib.load('comment_tfidf_vectorizer.pkl')

# Ví dụ: Dự đoán một bình luận
sample_comment = "click this link to get gift"
sample_comment_clean = preprocess_text(sample_comment)
sample_comment_vec = comment_vectorizer.transform([sample_comment_clean])
comment_prediction = comment_model.predict(sample_comment_vec)
print("Comment prediction:", comment_prediction)









# --- Đối với Post Spam ---
# Load mô hình và vectorizer cho post spam
post_model = joblib.load('post_spam_model.pkl')
post_vectorizer = joblib.load('post_tfidf_vectorizer.pkl')

# Ví dụ: Dự đoán một bài đăng
sample_post = "Cảm ơn đơn đặt hàng nhạc chuông của bạn, T91 tham khảo"
sample_post_clean = preprocess_text(sample_post)
sample_post_vec = post_vectorizer.transform([sample_post_clean])
post_prediction = post_model.predict(sample_post_vec)
print("Post prediction:", post_prediction)
