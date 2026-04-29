package com.example.domain.catalog

data class MoodEmoji(
    val id: String,
    val label: String
)

object MoodEmojiCatalog {
    val moods = listOf(
        MoodEmoji("feliicidad", "Feliz"),
        MoodEmoji("emoji_tristesa", "Triste"),
        MoodEmoji("emoji_enojo", "Enojado"),
        MoodEmoji("emoji_mudo", "Mudo"),
        MoodEmoji("emoji_desinteres", "Desinteres"),
        MoodEmoji("emoji_contento", "Contento"),
        MoodEmoji("emoji_miedo", "Ansioso"),
        MoodEmoji("emoji_terror", "Asustado"),
        MoodEmoji("emoji_pensativo", "Pensativo"),
        MoodEmoji("emoji_aburrido", "Aburrido"),
        MoodEmoji("emoji_desconfianza", "Desconfianza"),
        MoodEmoji("emoji_llanto", "Llanto"),
        MoodEmoji("emoji_ira", "Estresado"),
        MoodEmoji("emoji_desesperanza", "Solo"),
        MoodEmoji("emoji_malicia", "Malicioso"),
        MoodEmoji("emoji_rencor", "Rencoroso"),
        MoodEmoji("emoji_vengativo", "Vengativo"),
        MoodEmoji("emoji_frustraci_n", "Frustrado"),
        MoodEmoji("emoji_rechazo", "Rechazo"),
        MoodEmoji("emoji_alegria", "Divertido"),
        MoodEmoji("emoji_asco", "Enfermo"),
        MoodEmoji("emoji_pereza", "Somnoliento"),
        MoodEmoji("emoji_nerviosismo", "Preocupado"),
        MoodEmoji("emoji_enamoramiento", "Romántico"),
        MoodEmoji("emoji_desiluci_n", "Desilucionado"),
        MoodEmoji("emoji_maldiciendo", "Maldiciendo"),
        MoodEmoji("emoji_agradecimiento", "Agradecido"),
        MoodEmoji("emoji_juicioso", "Juicioso"),
        MoodEmoji("emoji_anonadado", "Anonadado"),
        MoodEmoji("emoji_interes", "Interesado")
    )
}
