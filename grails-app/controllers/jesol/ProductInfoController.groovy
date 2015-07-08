package jesol

import common.controller.JSONRestfulController

class ProductInfoController extends JSONRestfulController<ProductInfo> {

    ProductInfoController() {
        super(ProductInfo)
    }
}
