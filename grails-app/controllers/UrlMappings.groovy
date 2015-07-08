class UrlMappings {

    static mappings = {

        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/categories"(resources: "Category")
        "/fieldInfo"(resources: "FieldInfo")

        "/products"(resources:"Product") {
            "/productInfos"(resources: "ProductInfo")
        }
        "/productInfos"(resources: "ProductInfo")

        "/reviews"(resources: "Review")

        "/"(view:"/index")
        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}
