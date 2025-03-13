package kma.ktlt.post.domain.notification;

public enum NotificationTemplate {

    // Ai đó đăng bài viết mới
    NEW_POST("<b>%s</b> đã đăng một bài viết: <i>\"%s\"</i> Hãy tương tác ngay!"),
    NEW_LIKE_1("<b>%s</b> đã thích bài viết của bạn: <i>\"%s\"</i>"),
    NEW_LIKE_N("<b>%s</b> và <b>%s</b> người khác  đã thích bài viết của bạn: <i>\"%s\"</i>"),
    NEW_COMMENT_1("<b>%s</b> đã bình luận trên bài viết của bạn: <i>\"%s\"</i>"),
    NEW_COMMENT_N("<b>%s</b> và <b>%s</b> người khác đã bình luận trên bài viết của bạn: <i>\"%s\"</i>"),

    NEW_COMMENT_TO_OTHER_1("<b>%s</b> đã bình luận trên bài viết bạn đã bình luận: <i>\"%s\"</i>"),
    NEW_COMMENT_TO_OTHER_N("<b>%s</b> và <b>%s</b> người khác đã bình luận trên bài viết bạn đã bình luận: <i>\"%s\"</i>"),


    REPLY_COMMENT_1("<b>%s</b> đã trả lời bình luận của bạn: <i>\"%s\"</i>"),
    REPLY_COMMENT_N("<b>%s</b> và <b>%s</b> người khác đã trả lời bình luận của bạn: <i>\"%s\"</i>"),

    REPLY_COMMENT_TO_OTHER_1("<b>%s</b> đã trả lời một bình luận mà bạn cũng đã tham gia: <i>\"%s\"</i>"),
    REPLY_COMMENT_TO_OTHER_N("<b>%s</b> và <b>%s</b> người khác đã trả lời một bình luận mà bạn cũng đã tham gia: <i>\"%s\"</i>");






    private final String template;

    NotificationTemplate(String template) {
        this.template = template;
    }

    public String format(Object... args) {
        return String.format(template, args);
    }
}
