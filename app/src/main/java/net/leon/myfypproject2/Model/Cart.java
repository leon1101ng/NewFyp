package net.leon.myfypproject2.Model;

public class Cart {
    private String ProductID,ProductImage,ProductName,ProductsUserID;

    public String getProductsUserID() {
        return ProductsUserID;
    }

    public void setProductsUserID(String productsUserID) {
        ProductsUserID = productsUserID;
    }

    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }

    public String getProductImage() {
        return ProductImage;
    }

    public void setProductImage(String productImage) {
        ProductImage = productImage;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public int getProductPrice() {
        return ProductPrice;
    }

    public void setProductPrice(int productPrice) {
        ProductPrice = productPrice;
    }

    public int getProductQuantity() {
        return ProductQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        ProductQuantity = productQuantity;
    }

    public Cart(String productID, String productImage, String productName, String productsUserID, int productPrice, int productQuantity) {
        ProductID = productID;
        ProductImage = productImage;
        ProductName = productName;
        ProductsUserID = productsUserID;
        ProductPrice = productPrice;
        ProductQuantity = productQuantity;
    }

    private int ProductPrice;
    private int ProductQuantity;

    public Cart() {
    }




}
