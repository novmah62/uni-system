# comment_spam.py

import pandas as pd
import re
from sklearn.model_selection import train_test_split
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.linear_model import LogisticRegression
from sklearn.metrics import classification_report, confusion_matrix
import joblib

def preprocess_text(text):
    """
    Tiền xử lý văn bản: chuyển về chữ thường, loại bỏ ký tự đặc biệt và khoảng trắng thừa.
    """
    text = text.lower()
    # Loại bỏ ký tự không phải chữ, số hoặc khoảng trắng
    text = re.sub(r'[^a-z0-9\s]', '', text)
    text = re.sub(r'\s+', ' ', text).strip()
    return text

def main():
    # Đường dẫn tới file comment spam CSV
    comment_file = '/Users/ManhAT18A/Desktop/ML/data/comment_spam.csv'
    df_comment = pd.read_csv(comment_file)
    print("Comment Spam Dataset Columns:", df_comment.columns.tolist())
    
    # Xây dựng DataFrame với 2 cột: text (nội dung) và label (spam hoặc ham)
    # Giả sử: CLASS = 1 là spam, các giá trị khác là ham
    df = pd.DataFrame({
        'text': df_comment['CONTENT'],
        'label': df_comment['CLASS'].apply(lambda x: 'spam' if x == 1 else 'ham')
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
    joblib.dump(model, 'comment_spam_model.pkl')
    joblib.dump(vectorizer, 'comment_tfidf_vectorizer.pkl')
    print("Comment spam model and vectorizer saved.")

if __name__ == '__main__':
    main()

# ------------------------------------------------------------------------------------

# import pandas as pd
# import re
# import joblib
# from sklearn.model_selection import train_test_split
# from sklearn.feature_extraction.text import TfidfVectorizer
# from sklearn.linear_model import LogisticRegression
# from sklearn.naive_bayes import MultinomialNB
# from sklearn.svm import SVC
# from sklearn.ensemble import RandomForestClassifier
# from sklearn.metrics import classification_report, confusion_matrix

# def preprocess_text(text):
#     """
#     Tiền xử lý văn bản: chuyển về chữ thường, loại bỏ ký tự đặc biệt và khoảng trắng thừa.
#     """
#     text = text.lower()
#     text = re.sub(r'[^a-z0-9\s]', '', text)
#     text = re.sub(r'\s+', ' ', text).strip()
#     return text

# def main():
#     # Đường dẫn tới file comment spam CSV
#     comment_file = '/Users/ManhAT18A/Desktop/ML/data/comment_spam.csv'
#     df_comment = pd.read_csv(comment_file)
#     print("Comment Spam Dataset Columns:", df_comment.columns.tolist())
    
#     # Xây dựng DataFrame với 2 cột: text (nội dung) và label (spam hoặc ham)
#     df = pd.DataFrame({
#         'text': df_comment['CONTENT'],
#         'label': df_comment['CLASS'].apply(lambda x: 'spam' if x == 1 else 'ham')
#     })
    
#     # Tiền xử lý văn bản
#     df['text_clean'] = df['text'].apply(preprocess_text)
#     print("Sample processed text:\n", df[['text', 'text_clean', 'label']].head())
    
#     # Chuẩn bị dữ liệu huấn luyện
#     X = df['text_clean']
#     y = df['label']
    
#     X_train, X_test, y_train, y_test = train_test_split(
#         X, y, test_size=0.2, random_state=42, stratify=y
#     )
    
#     # Vector hóa văn bản sử dụng TF-IDF
#     vectorizer = TfidfVectorizer(stop_words='english', max_features=5000)
#     X_train_vec = vectorizer.fit_transform(X_train)
#     X_test_vec = vectorizer.transform(X_test)
    
#     # Danh sách các mô hình
#     models = {
#         'Logistic Regression': LogisticRegression(max_iter=1000),
#         'Naive Bayes': MultinomialNB(),
#         'SVM': SVC(kernel='linear', probability=True),
#         'Random Forest': RandomForestClassifier(n_estimators=100, random_state=42)
#     }
    
#     # Huấn luyện, đánh giá và lưu từng mô hình
#     for name, model in models.items():
#         print(f"\n🔹 Đang huấn luyện mô hình: {name}")
#         model.fit(X_train_vec, y_train)
#         y_pred = model.predict(X_test_vec)
        
#         print("Confusion Matrix:")
#         print(confusion_matrix(y_test, y_pred))
#         print("\nClassification Report:")
#         print(classification_report(y_test, y_pred))
        
#         # Lưu mô hình
#         model_filename = f'{name.replace(" ", "_").lower()}_model.pkl'
#         joblib.dump(model, model_filename)
#         print(f"✅ Đã lưu {name} vào {model_filename}")
    
#     # Lưu vectorizer
#     joblib.dump(vectorizer, 'comment_tfidf_vectorizer.pkl')
#     print("✅ Đã lưu TF-IDF vectorizer.")
    
#     # Đánh giá chung
#     print("\n🔍 Nhận xét từng mô hình:")
#     print("- Logistic Regression: Đơn giản, hiệu quả với dữ liệu nhỏ, nhanh chóng.")
#     print("- Naive Bayes: Tốt cho xử lý văn bản, nhưng giả định độc lập điều kiện có thể không chính xác.")
#     print("- SVM: Mạnh mẽ, nhưng chậm với tập dữ liệu lớn.")
#     print("- Random Forest: Tốt cho dữ liệu phức tạp, nhưng tiêu tốn tài nguyên hơn.")

# if __name__ == '__main__':
#     main()


# ------------------------------------------------------------------------------------


# import pandas as pd
# import re
# import joblib
# import matplotlib.pyplot as plt
# import seaborn as sns
# from sklearn.model_selection import train_test_split
# from sklearn.feature_extraction.text import TfidfVectorizer
# from sklearn.linear_model import LogisticRegression
# from sklearn.naive_bayes import MultinomialNB
# from sklearn.svm import SVC
# from sklearn.ensemble import RandomForestClassifier
# from sklearn.metrics import classification_report, accuracy_score

# def preprocess_text(text):
#     """Tiền xử lý văn bản"""
#     text = text.lower()
#     text = re.sub(r'[^a-z0-9\s]', '', text)
#     text = re.sub(r'\s+', ' ', text).strip()
#     return text

# def train_and_evaluate(models, X_train_vec, X_test_vec, y_train, y_test):
#     """Huấn luyện và đánh giá nhiều mô hình"""
#     results = []
    
#     for name, model in models.items():
#         print(f"🔹 Đang huấn luyện mô hình: {name}")
#         model.fit(X_train_vec, y_train)
#         y_pred = model.predict(X_test_vec)
        
#         acc = accuracy_score(y_test, y_pred)
#         report = classification_report(y_test, y_pred, output_dict=True)
        
#         print(f"📊 Đánh giá {name}:\n", classification_report(y_test, y_pred))
        
#         # Lưu model
#         joblib.dump(model, f'{name.lower().replace(" ", "_")}_model.pkl')
        
#         # Ghi nhận kết quả
#         results.append({
#             'Model': name,
#             'Accuracy': acc,
#             'Precision (Spam)': report['spam']['precision'],
#             'Recall (Spam)': report['spam']['recall'],
#             'F1-score (Spam)': report['spam']['f1-score'],
#         })
    
#     return pd.DataFrame(results)

# def plot_results(results_df):
#     """Vẽ biểu đồ so sánh mô hình"""
#     plt.figure(figsize=(10, 5))
#     sns.barplot(x='Model', y='Accuracy', data=results_df, palette='viridis')
#     plt.title('So sánh Accuracy giữa các mô hình')
#     plt.ylabel('Accuracy')
#     plt.ylim(0, 1)
#     plt.xticks(rotation=20)
#     plt.show()

#     plt.figure(figsize=(10, 5))
#     results_df.set_index('Model')[['Precision (Spam)', 'Recall (Spam)', 'F1-score (Spam)']].plot(kind='bar')
#     plt.title('So sánh Precision, Recall, F1-score giữa các mô hình')
#     plt.ylabel('Score')
#     plt.ylim(0, 1)
#     plt.xticks(rotation=20)
#     plt.legend(loc='lower right')
#     plt.show()

# def main():
#     comment_file = '/Users/ManhAT18A/Desktop/ML/data/comment_spam.csv'
#     df_comment = pd.read_csv(comment_file)
    
#     df = pd.DataFrame({
#         'text': df_comment['CONTENT'],
#         'label': df_comment['CLASS'].apply(lambda x: 'spam' if x == 1 else 'ham')
#     })
    
#     df['text_clean'] = df['text'].apply(preprocess_text)

#     X = df['text_clean']
#     y = df['label']

#     X_train, X_test, y_train, y_test = train_test_split(
#         X, y, test_size=0.2, random_state=42, stratify=y
#     )

#     vectorizer = TfidfVectorizer(stop_words='english', max_features=5000)
#     X_train_vec = vectorizer.fit_transform(X_train)
#     X_test_vec = vectorizer.transform(X_test)
    
#     # Lưu vectorizer
#     joblib.dump(vectorizer, 'comment_tfidf_vectorizer.pkl')

#     models = {
#         'Logistic Regression': LogisticRegression(max_iter=1000),
#         'Naive Bayes': MultinomialNB(),
#         'SVM': SVC(kernel='linear'),
#         'Random Forest': RandomForestClassifier(n_estimators=100),
#     }
    
#     results_df = train_and_evaluate(models, X_train_vec, X_test_vec, y_train, y_test)
    
#     print("\n📌 Bảng so sánh mô hình:")
#     print(results_df)
    
#     # Vẽ biểu đồ
#     plot_results(results_df)

# if __name__ == '__main__':
#     main()
