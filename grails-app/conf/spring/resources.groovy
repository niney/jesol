import jesol.CorsFilter
import org.springframework.boot.context.embedded.FilterRegistrationBean
import org.springframework.core.Ordered

// Place your Spring DSL code here
beans = {
//    corsFilter(CorsFilter)
    corsFilter(FilterRegistrationBean) {
        filter = bean(CorsFilter)
        urlPatterns = ['/*']
        order = Ordered.HIGHEST_PRECEDENCE
    }
}
