package net.leon.myfypproject2.Model;

public class ProductsClass {
    private String ProductsCategorys,ProductsDescription,ProductsImage,ProductsImage2,ProductsImage3,ProductsName,ProductsUser,UserImage;

    public ProductsClass(String productsCategorys, String productsDescription, String productsImage, String productsImage2, String productsImage3, String productsName, String productsUser, String userImage, int productsPrice, int productsQuantity) {
        ProductsCategorys = productsCategorys;
        ProductsDescription = productsDescription;
        ProductsImage = productsImage;
        ProductsImage2 = productsImage2;
        ProductsImage3 = productsImage3;
        ProductsName = productsName;
        ProductsUser = productsUser;
        UserImage = userImage;
        ProductsPrice = productsPrice;
        ProductsQuantity = productsQuantity;
    }

    private int ProductsPrice,ProductsQuantity;

    public String getProductsCategorys() {
        return ProductsCategorys;
    }

    public void setProductsCategorys(String productsCategorys) {
        ProductsCategorys = productsCategorys;
    }

    public String getProductsDescription() {
        return ProductsDescription;
    }

    public void setProductsDescription(String productsDescription) {
        ProductsDescription = productsDescription;
    }

    public String getProductsImage() {
        return ProductsImage;
    }

    public void setProductsImage(String productsImage) {
        ProductsImage = productsImage;
    }

    public String getProductsImage2() {
        return ProductsImage2;
    }

    public void setProductsImage2(String productsImage2) {
        ProductsImage2 = productsImage2;
    }

    public String getProductsImage3() {
        return ProductsImage3;
    }

    public void setProductsImage3(String productsImage3) {
        ProductsImage3 = productsImage3;
    }

    public String getProductsName() {
        return ProductsName;
    }

    public void setProductsName(String productsName) {
        ProductsName = productsName;
    }

    public String getProductsUser() {
        return ProductsUser;
    }

    public void setProductsUser(String productsUser) {
        ProductsUser = productsUser;
    }

    public String getUserImage() {
        return UserImage;
    }

    public void setUserImage(String userImage) {
        UserImage = userImage;
    }

    public int getProductsPrice() {
        return ProductsPrice;
    }

    public void setProductsPrice(int productsPrice) {
        ProductsPrice = productsPrice;
    }

    public int getProductsQuantity() {
        return ProductsQuantity;
    }

    public void setProductsQuantity(int productsQuantity) {
        ProductsQuantity = productsQuantity;
    }

    public ProductsClass() {
    }
}
