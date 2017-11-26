package by.heap.wth3

import com.leapmotion.leap.Controller
import com.leapmotion.leap.Hand
import com.leapmotion.leap.Listener
import java.awt.Robot
import java.awt.event.KeyEvent
import java.io.IOException
import kotlin.concurrent.thread

object App {
    @JvmStatic
    fun main(args: Array<String>) {
        when (args.getOrNull(0)) {
            "mario" -> Mario.main(arrayOf())
            "chrome" -> Chrome.main(arrayOf())
            "ninja" -> Ninja.main(arrayOf())
        }
    }
}

object Mario {
    @JvmStatic
    fun main(args: Array<String>) {
        val controller = Controller()
        val boobsListeners = BoobsListener(
            EventHandler(
                Robot()
            )
        )
        controller.addListener(boobsListeners)

        // Keep this process running until Enter is pressed
        println("Press Enter to quit...")
        try {
            System.`in`.read()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        controller.removeListener(boobsListeners)
    }
}

object Chrome {
    @JvmStatic
    fun main(args: Array<String>) {
        val controller = Controller()
        val chromeListener = ChromeListener(
            EventHandler(
                Robot()
            )
        )
        controller.addListener(chromeListener)

        // Keep this process running until Enter is pressed
        println("Press Enter to quit...")
        try {
            System.`in`.read()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        controller.removeListener(chromeListener)
    }
}

object Ninja {
    @JvmStatic
    fun main(args: Array<String>) {
        val controller = Controller()
        val ninjaListener = NinjaListener(
            EventHandler(
                Robot()
            )
        )
        controller.addListener(ninjaListener)

        // Keep this process running until Enter is pressed
        println("Press Enter to quit...")
        try {
            System.`in`.read()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        controller.removeListener(ninjaListener)
    }
}

sealed class BoobsEvent
class Jump(val state: Boolean) : BoobsEvent() {
    companion object {
        private val press = Jump(true)
        private val release = Jump(false)

        fun of(state: Boolean): Jump {
            return if (state) press else release
        }
    }
}
class MoveRight(val state: Boolean) : BoobsEvent() {
    companion object {
        private val press = MoveRight(true)
        private val release = MoveRight(false)

        fun of(state: Boolean): MoveRight {
            return if (state) press else release
        }
    }
}
class Space(val state: Boolean) : BoobsEvent() {
    companion object {
        private val press = Space(true)
        private val release = Space(false)

        fun of(state: Boolean): Space {
            return if (state) press else release
        }
    }
}


class EventHandler(
    private val robot: Robot
) {

    init {
        Runtime.getRuntime().addShutdownHook(Thread {
            println("Shutdown")
            robot.keyRelease(KeyEvent.VK_RIGHT)
            robot.keyRelease(KeyEvent.VK_LEFT)
            robot.keyRelease(KeyEvent.VK_D)
            robot.keyRelease(KeyEvent.VK_SPACE)
        })
    }

    fun handle(event: BoobsEvent) {
        when (event) {

            is Jump -> if (event.state) {
                robot.keyPress(KeyEvent.VK_F)
            } else {
                robot.keyRelease(KeyEvent.VK_F)
            }

            is MoveRight -> if (event.state) {
                robot.keyPress(KeyEvent.VK_RIGHT)
                robot.keyPress(KeyEvent.VK_D)
            } else {
                robot.keyRelease(KeyEvent.VK_RIGHT)
                robot.keyRelease(KeyEvent.VK_D)
            }

            is Space -> if (event.state) {
                robot.keyPress(KeyEvent.VK_SPACE)
            } else {
              robot.keyRelease(KeyEvent.VK_SPACE)
            }
        }
    }

}

class BoobsListener(
    private val handler: EventHandler
) : Listener() {

    override fun onFrame(controller: Controller) {
        val frame = controller.frame()

        val hands = frame.hands()

        val left: Hand? = hands.leftmost()
        val right: Hand? = hands.rightmost()

        if (left != null) {
            handler.handle(MoveRight.of(left.palmPosition().y < 180))
        }

        if (frame.id() % 8 == 0L && right != null) {
            handler.handle(Jump.of(right.palmPosition().y < 170))
        }
    }
}

class ChromeListener(
    private val handler: EventHandler
) : Listener() {

    override fun onFrame(controller: Controller) {
        val frame = controller.frame()
        val hands = frame.hands()

        val left: Hand? = hands.leftmost()
        val right: Hand? = hands.rightmost()

        handler.handle(Space.of(left != null && left.palmPosition().y < 135))
        handler.handle(Space.of(right != null && right.palmPosition().y < 135))
    }
}

class NinjaListener(
    private val handler: EventHandler
) : Listener() {

    private var leftPressed = false
    private var rightPressed = false

    override fun onFrame(controller: Controller) {
        val frame = controller.frame()
        val hands = frame.hands()

        val left: Hand? = hands.leftmost()
        val right: Hand? = hands.rightmost()

        if (!leftPressed && left != null && left.palmPosition().y < 160  ) {
            leftPressed = true
            handler.handle(Space.of(true))
            thread(start = true) {
                Thread.sleep(50)
                 handler.handle(Space.of(false))
            }
        } else if (leftPressed && left != null && left.palmPosition().y >= 160  ) {
            leftPressed = false
        }


        if (!rightPressed && right != null && right.palmPosition().y < 160  ) {
            rightPressed = true
            handler.handle(Space.of(true))
            thread(start = true) {
                Thread.sleep(50)
                handler.handle(Space.of(false))
            }
        } else if (rightPressed && right != null && right.palmPosition().y >= 160  ) {
            rightPressed = false
        }
    }
}
