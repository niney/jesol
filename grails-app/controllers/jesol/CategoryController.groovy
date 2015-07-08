package jesol

import common.controller.JSONRestfulController
import grails.transaction.Transactional

@Transactional(readOnly = true)
class CategoryController extends JSONRestfulController<Category> {

    CategoryController() {
        super(Category)
    }

    @Override
    def index(Integer max) {
        super.searchIndex(max)
    }
}
