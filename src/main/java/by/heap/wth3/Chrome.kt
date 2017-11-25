package by.heap.wth3

import com.leapmotion.leap.Controller
import com.leapmotion.leap.Gesture
import com.leapmotion.leap.Hand
import com.leapmotion.leap.Listener
import java.awt.Robot
import java.awt.event.KeyEvent
import java.io.IOException

/**
 * TODO.
 *
 * @author Ibragimov Ruslan
 * @since TODO
 */

object Chrome {
    @JvmStatic
    fun main(args: Array<String>) {
        val controller = Controller()
        val boobsListeners = ChromeListeners(
            ChromeHandler(
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

sealed class ChromeEvent
class Space(val state: Boolean) : ChromeEvent()

class ChromeListeners(
    private val handler: ChromeHandler
) : Listener() {

    override fun onConnect(controller: Controller) {
        controller.enableGesture(Gesture.Type.TYPE_SWIPE)
    }

    override fun onFrame(ctrl: Controller) {
        val frame = ctrl.frame()
        val hands = frame.hands()

        val left: Hand? = hands.find { it.isLeft };
        val right: Hand? = hands.find { it.isRight }

        handler.handle(Space(left != null && left.palmPosition().y < 135))
        handler.handle(Space(right != null && right.palmPosition().y < 135))
    }
}

class ChromeHandler(
    private val robot: Robot
) {
    init {
        Runtime.getRuntime().addShutdownHook(object : Thread() {
            override fun run() {
                println("Shutdown")
                robot.keyRelease(KeyEvent.VK_RIGHT)
                robot.keyRelease(KeyEvent.VK_LEFT)
                robot.keyRelease(KeyEvent.VK_D)
            }
        })
    }

    fun handle(event: ChromeEvent) {
        when (event) {
            is Space -> if (event.state) {
                robot.keyPress(KeyEvent.VK_SPACE)
            } else {
                robot.keyRelease(KeyEvent.VK_SPACE)
            }
        }
    }

}