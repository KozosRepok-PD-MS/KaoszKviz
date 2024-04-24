
package hu.kaoszkviz.server.api.jsonview;


public enum JsonViewEnum {
    PRIVATE_VIEW(new PrivateJsonView() {
    }),
    PUBLIC_VIEW(new PublicJsonView() {
    });
    
    public final JsonViewMark viewClass;
    
    private JsonViewEnum(JsonViewMark view) {
        this.viewClass = view;
    }
}
