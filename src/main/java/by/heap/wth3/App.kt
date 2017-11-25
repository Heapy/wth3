package by.heap.wth3

import com.leapmotion.leap.Controller
import com.leapmotion.leap.Hand
import com.leapmotion.leap.Listener
import java.awt.Robot
import java.awt.event.KeyEvent
import java.io.IOException

object App {
    @JvmStatic
    fun main(args: Array<String>) {
        val controller = Controller()
        val boobsListeners = BoobsListeners(
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

sealed class BoobsEvent
class Jump(val state: Boolean) : BoobsEvent()
class MoveRight(val state: Boolean) : BoobsEvent()


/*
Frame id: 76673, timestamp: 1511544469983934, hands: 1, fingers: 5, tools: 0, gestures 0
  Right hand, id: 76, palm position: (-11.4325, 121.024, 3.72267)
  pitch: 13.472581435022356 degrees, roll: -6.704240908604539 degrees, yaw: 72.93224086301923 degrees
  Arm direction: (0.893393, 0.0483557, -0.446667), wrist position: (-54.8291, 117.312, 25.1722), elbow position: (-268.901, 105.726, 132.201)
    TYPE_THUMB, id: 760, length: 46.25175mm, width: 17.971281mm
      TYPE_METACARPAL bone, start: (-65.8675, 122.424, 0.81641), end: (-65.8675, 122.424, 0.81641), direction: (0, 0, 0)
      TYPE_PROXIMAL bone, start: (-65.8675, 122.424, 0.81641), end: (-58.6718, 144.308, -37.0232), direction: (-0.16243, -0.493992, 0.854159)
      TYPE_INTERMEDIATE bone, start: (-58.6718, 144.308, -37.0232), end: (-40.5832, 144.954, -61.2715), direction: (-0.597795, -0.021348, 0.801364)
      TYPE_DISTAL bone, start: (-40.5832, 144.954, -61.2715), end: (-24.2668, 134.876, -69.2461), direction: (-0.785574, 0.485242, 0.383946)
    TYPE_INDEX, id: 761, length: 52.18994mm, width: 17.166168mm
      TYPE_METACARPAL bone, start: (-53.0542, 133.952, 12.877), end: (3.60144, 135.986, -19.5097), direction: (-0.867743, -0.0311524, 0.496037)
      TYPE_PROXIMAL bone, start: (3.60144, 135.986, -19.5097), end: (40.03, 131.357, -29.7694), direction: (-0.955432, 0.121422, 0.269086)
      TYPE_INTERMEDIATE bone, start: (40.03, 131.357, -29.7694), end: (59.7725, 126.579, -36.6636), direction: (-0.920374, 0.222737, 0.321403)
      TYPE_DISTAL bone, start: (59.7725, 126.579, -36.6636), end: (73.1542, 121.997, -42.1268), direction: (-0.882528, 0.302209, 0.360298)
    TYPE_MIDDLE, id: 762, length: 59.46625mm, width: 16.859457mm
      TYPE_METACARPAL bone, start: (-47.4926, 129.74, 21.2877), end: (9.44088, 127.244, -2.92108), direction: (-0.919512, 0.0403192, 0.390988)
      TYPE_PROXIMAL bone, start: (9.44088, 127.244, -2.92108), end: (51.1792, 120.018, -8.88167), direction: (-0.975731, 0.168914, 0.139343)
      TYPE_INTERMEDIATE bone, start: (51.1792, 120.018, -8.88167), end: (75.3094, 114.28, -13.5381), direction: (-0.956166, 0.227382, 0.184511)
      TYPE_DISTAL bone, start: (75.3094, 114.28, -13.5381), end: (90.9186, 109.705, -17.2197), direction: (-0.93595, 0.274346, 0.220754)
    TYPE_RING, id: 763, length: 57.178387mm, width: 16.042843mm
      TYPE_METACARPAL bone, start: (-43.6333, 123.27, 29.1691), end: (9.49412, 116.553, 14.2447), direction: (-0.95568, 0.120832, 0.268467)
      TYPE_PROXIMAL bone, start: (9.49412, 116.553, 14.2447), end: (48.2564, 117.984, 22.4733), direction: (-0.977565, -0.0360847, -0.207521)
      TYPE_INTERMEDIATE bone, start: (48.2564, 117.984, 22.4733), end: (70.2631, 107.962, 18.0373), direction: (-0.895138, 0.407639, 0.180437)
      TYPE_DISTAL bone, start: (70.2631, 107.962, 18.0373), end: (79.1944, 96.513, 10.0313), direction: (-0.53863, 0.690478, 0.482824)
    TYPE_PINKY, id: 764, length: 44.826797mm, width: 14.250507mm
      TYPE_METACARPAL bone, start: (-43.2744, 111.926, 34.7116), end: (7.08585, 103.71, 28.0417), direction: (-0.978626, 0.15966, 0.129613)
      TYPE_PROXIMAL bone, start: (7.08585, 103.71, 28.0417), end: (34.6586, 90.8142, 35.6682), direction: (-0.878664, 0.410955, -0.243034)
      TYPE_INTERMEDIATE bone, start: (34.6586, 90.8142, 35.6682), end: (49.3103, 81.6619, 37.3581), direction: (-0.844102, 0.52727, -0.0973597)
      TYPE_DISTAL bone, start: (49.3103, 81.6619, 37.3581), end: (61.4319, 72.3393, 36.9574), direction: (-0.792405, 0.609432, 0.0261979)
*/
class BoobsListeners(
    private val handler: EventHandler
) : Listener() {

    override fun onFrame(ctrl: Controller) {
        val frame = ctrl.frame()

        val hands = frame.hands()

        val left: Hand? = hands.leftmost()
        val right: Hand? = hands.rightmost()

        if (left != null) {
            handler.handle(MoveRight(left.palmPosition().y < 180))
        }

        if (frame.id() % 8 == 0L && right != null) {
            handler.handle(Jump(right.palmPosition().y < 170))
        }
    }
}

class EventHandler(
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
        }
    }

}