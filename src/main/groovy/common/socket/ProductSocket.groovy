package common.socket

import jesol.Product
import jesol.SampleVerticle
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.ComponentScan
import org.springframework.stereotype.Component

/**
 * Created by Niney on 2015-06-02.
 */
@ComponentScan("jesol")
@Component
class ProductSocket extends NoticeSocket<Product> {

    @Autowired
    ProductSocket(SampleVerticle sampleVerticle) {
        super(sampleVerticle)
    }
}
