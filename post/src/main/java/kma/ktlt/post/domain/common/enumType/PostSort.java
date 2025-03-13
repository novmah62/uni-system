    package kma.ktlt.post.domain.common.enumType;

    public enum PostSort {

        UPDATED_ASC("updated_at", "asc"),
        UPDATED_DESC("updated_at", "desc"),
        LIKE_ASC("like", "asc"),
        LIKE_DESC("like", "desc"),

        COMMENT_ASC("comment", "asc"),
        COMMENT_DESC("comment", "desc");
        private final String field;
        private final String direction;

        PostSort(String field, String direction) {
            this.field = field;
            this.direction = direction;
        }

        public String getField() {
            return field;
        }

        public String getDirection() {
            return direction;
        }
    }
