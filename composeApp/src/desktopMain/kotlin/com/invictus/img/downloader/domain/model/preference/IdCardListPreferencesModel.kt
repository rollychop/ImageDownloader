package ins.quivertech.app.domain.model.preference

data class IdCardListPreferencesModel(
    val sortOrder: SortOrder = SortOrder.Asc,
    val sortBy: SortBy = SortBy.RollNo,
    val imageSize: PreviewImageSize = PreviewImageSize.Small,
    val listItemType: ListItemType = ListItemType.Grid
)