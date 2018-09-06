import org.springframework.cloud.contract.spec.Contract
import org.springframework.cloud.contract.spec.internal.HttpMethods

Contract.make {
    request {
        method HttpMethods.HttpMethod.GET
        url "/customers"
    }
    response {
        body(
        """
            [ 
            { "id": 1, "name" : "Jane" },
            { "id": 2, "name" : "John" }
            ] 
        """
        )
        status(200)
        headers {
            contentType(applicationJsonUtf8())
        }
    }
}