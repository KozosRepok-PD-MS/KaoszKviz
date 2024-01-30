
package hu.kaoszkviz.server.api.jsonview;


public enum JsonViewEnum {
    PRIVATE_VIEW(new PrivateJsonView() {
    }),
    PUBLIC_VIEW(new PublicJsonView() {
    });
    
    public final JsonView viewClass;
    
    private JsonViewEnum(JsonView view) {
        this.viewClass = view;
    }
}
