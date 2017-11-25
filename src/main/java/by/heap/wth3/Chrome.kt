package by.heap.wth3

/**
 * TODO.
 *
 * @author Ibragimov Ruslan
 * @since TODO
 */

//object Chrome {
//    @JvmStatic
//    fun main(args: Array<String>) {
//        val controller = Controller()
//        val boobsListeners = ChromeListener(
//            ChromeHandler(
//                Robot()
//            )
//        )
//        controller.addListener(boobsListeners)
//
//        // Keep this process running until Enter is pressed
//        println("Press Enter to quit...")
//        try {
//            System.`in`.read()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//
//        controller.removeListener(boobsListeners)
//    }
//}

//sealed class ChromeEvent
//class Space(val state: Boolean) : ChromeEvent()

//class ChromeListener(
//    private val handler: ChromeHandler
//) : Listener() {
//
//    override fun onFrame(ctrl: Controller) {
//        val frame = ctrl.frame()
//        val hands = frame.hands()
//
//        val left: Hand? = hands.leftmost()
//        val right: Hand? = hands.rightmost()
//
//        handler.handle(Space(left != null && left.palmPosition().y < 135))
//        handler.handle(Space(right != null && right.palmPosition().y < 135))
//    }
//}

//class ChromeHandler(
//    private val robot: Robot
//) {
//    init {
//        Runtime.getRuntime().addShutdownHook(object : Thread() {
//            override fun run() {
//                println("Shutdown")
//                robot.keyRelease(KeyEvent.VK_SPACE)
//            }
//        })
//    }
//
//    fun handle(event: ChromeEvent) {
//        when (event) {
//            is Space -> if (event.state) {
//                robot.keyPress(KeyEvent.VK_SPACE)
//            } else {
//                robot.keyRelease(KeyEvent.VK_SPACE)
//            }
//        }
//    }
//
//}