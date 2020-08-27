package jakdb.utils

@Synchronized
fun eightBall(max: Int): String {
    when ((Math.random() * max).toInt()) {
        1 -> return "Yes"
        2 -> return "No"
        3 -> return "Maybe"
        4 -> return "Ask again"
        5 -> return "I don't know"
        6 -> return "Maybe 666cube know it..."
        7 -> return "Ask your dog"
    }
    return "Don't ask me"
}