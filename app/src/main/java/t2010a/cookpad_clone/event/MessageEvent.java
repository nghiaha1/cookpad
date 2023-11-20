package t2010a.cookpad_clone.event;


import t2010a.cookpad_clone.model.client_model.Content;
import t2010a.cookpad_clone.model.shop.Product;

public class MessageEvent {

    public static class ProductEvent {
        private Product product;

        public ProductEvent(Product product) {
            this.product = product;
        }

        public Product getProduct() {
            return product;
        }

        public void setProduct(Product product) {
            this.product = product;
        }
    }

    public static class ProductToCartEvent {
        private Product product;

        public ProductToCartEvent(Product product) {
            this.product = product;
        }

        public Product getProduct() {
            return product;
        }

        public void setProduct(Product product) {
            this.product = product;
        }
    }

    public static class PostEvent {
        private Content post;

        public PostEvent(Content post) {
            this.post = post;
        }

        public Content getPost() {
            return post;
        }

        public void setPost(Content post) {
            this.post = post;
        }
    }

    public static class EditInactivePostEvent {
        private Content post;

        public EditInactivePostEvent(Content post) {
            this.post = post;
        }

        public Content getPost() {
            return post;
        }

        public void setPost(Content post) {
            this.post = post;
        }
    }

    public static class EditActivePostEvent {
        private Content post;

        public EditActivePostEvent(Content post) {
            this.post = post;
        }

        public Content getPost() {
            return post;
        }

        public void setPost(Content post) {
            this.post = post;
        }
    }

    public static class EditPendingPostEvent {
        private Content post;

        public EditPendingPostEvent(Content post) {
            this.post = post;
        }

        public Content getPost() {
            return post;
        }

        public void setPost(Content post) {
            this.post = post;
        }
    }

    public static class SearchFragmentEvent {
        private Content post;

        public SearchFragmentEvent(Content post) {
            this.post = post;
        }

        public Content getPost() {
            return post;
        }

        public void setPost(Content post) {
            this.post = post;
        }
    }
}
