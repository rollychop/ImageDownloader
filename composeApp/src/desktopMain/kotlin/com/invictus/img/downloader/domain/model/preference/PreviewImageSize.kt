package ins.quivertech.app.domain.model.preference

enum class PreviewImageSize(val sizeDp: Int, val title: String) {
    Small(80, "Small"),
    Medium(120, "Medium"),
    Large(160, "Large"),
    XLarge(200, "X-Large");
}