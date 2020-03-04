
import kotlin.random.Random

fun main(){
    val categories: MutableMap<String, Double> = mutableMapOf()
    val users: MutableList<User> = mutableListOf()
    fun generateRandomPassword(): String {
        val chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ#&@$€"
        var passWord = ""
        for (i in 0..7) {
            passWord += chars[Random.nextInt(chars.length)]
        }
        return passWord
    }

    val df = DataFa

    for(i in 0..100){
        val n = "Random lesz"
        val p = generateRandomPassword()
        val m = Random.nextInt(1,10)
        val s = Random.nextInt(1,10)
        val e = Random.nextInt(1,10)
        val d = Random.nextDouble(1.0,10.0)
        users.add(User(n, p, m, s, s, d, categories))
    }
}