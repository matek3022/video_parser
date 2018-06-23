import java.io.*
import java.util.*


val headChanel1 = arrayListOf(0, 0, 1, 228.toByte())
val headChanel2 = arrayListOf(0, 0, 1, 229.toByte())
val headChanel3 = arrayListOf(0, 0, 1, 230.toByte())
val headChanel4 = arrayListOf(0, 0, 1, 231.toByte())

val PES_HEADER_SIZE_FREE_BYTE = 28

var counter = 0
var fileSize = 47185920f

var threadIsRunning = true

var fileName = "20180619_092000spHm.ps"

fun main(args : Array<String>) {
    if (args.isEmpty()) {
        println("укажите имя файла ps БЕЗ расширения")
        return
    } else {
        fileName = args[0] + ".ps"
    }
    object : Thread() {
        override fun run() {
            super.run()
            while (threadIsRunning) {
                System.out.println("${(counter / fileSize) * 100}%")
                sleep(1000)
            }
        }
    }.start()
    try {
        val inputFile = File(fileName)
        fileSize = inputFile.length().toFloat()

        val outputFileName = fileName.split(".")[0] + "_output_stream_"
        val catalog = File(outputFileName)
        val outputFile1 = File(outputFileName + "1")
        val outputFile2 = File(outputFileName + "2")
        val outputFile3 = File(outputFileName + "3")
        val outputFile4 = File(outputFileName + "4")

        outputFile1.createNewFile()
        outputFile2.createNewFile()
        outputFile3.createNewFile()
        outputFile4.createNewFile()


        val fis = FileInputStream(inputFile)
        val fos1 = FileOutputStream(outputFile1)
        val fos2 = FileOutputStream(outputFile2)
        val fos3 = FileOutputStream(outputFile3)
        val fos4 = FileOutputStream(outputFile4)

        var readInt = fis.read()
        val lastFourByte = LinkedList<Byte>()

        while (readInt != -1) {
            if (headChanel1.equilsList(lastFourByte)) {
                for (i in 0 until PES_HEADER_SIZE_FREE_BYTE) readInt = fis.read()
                while ((!headChanel2.equilsList(lastFourByte)) && (!headChanel3.equilsList(lastFourByte)) && (!headChanel4.equilsList(lastFourByte)) && (readInt != -1)) {
                    readInt = fis.read()
                    lastFourByte.add(readInt.toByte())
                    if (lastFourByte.size > 4) lastFourByte.removeFirst()
                    fos1.write(readInt)
                    counter++
                }
            } else if (headChanel2.equilsList(lastFourByte)) {
                for (i in 0 until PES_HEADER_SIZE_FREE_BYTE) readInt = fis.read()
                while ((!headChanel1.equilsList(lastFourByte)) && (!headChanel3.equilsList(lastFourByte)) && (!headChanel4.equilsList(lastFourByte)) && (readInt != -1)) {
                    readInt = fis.read()
                    lastFourByte.add(readInt.toByte())
                    if (lastFourByte.size > 4) lastFourByte.removeFirst()
                    fos2.write(readInt)
                    counter++
                }
            } else if (headChanel3.equilsList(lastFourByte)) {
                for (i in 0 until PES_HEADER_SIZE_FREE_BYTE) readInt = fis.read()
                while ((!headChanel2.equilsList(lastFourByte)) && (!headChanel1.equilsList(lastFourByte)) && (!headChanel4.equilsList(lastFourByte)) && (readInt != -1)) {
                    readInt = fis.read()
                    lastFourByte.add(readInt.toByte())
                    if (lastFourByte.size > 4) lastFourByte.removeFirst()
                    fos3.write(readInt)
                    counter++
                }
            } else if (headChanel4.equilsList(lastFourByte)) {
                for (i in 0 until PES_HEADER_SIZE_FREE_BYTE) readInt = fis.read()
                while ((!headChanel2.equilsList(lastFourByte)) && (!headChanel3.equilsList(lastFourByte)) && (!headChanel1.equilsList(lastFourByte)) && (readInt != -1)) {
                    readInt = fis.read()
                    lastFourByte.add(readInt.toByte())
                    if (lastFourByte.size > 4) lastFourByte.removeFirst()
                    fos4.write(readInt)
                    counter++
                }
            } else {
                lastFourByte.add(readInt.toByte())
                if (lastFourByte.size > 4) lastFourByte.removeFirst()
                readInt = fis.read()
                counter++
            }
        }
        threadIsRunning = false
        fis.close()
        fos1.close()
        fos2.close()
        fos3.close()
        fos4.close()
    } catch (e: FileNotFoundException) {
        System.err.println("FileStreamsTest: " + e)
    } catch (e: IOException) {
        System.err.println("FileStreamsTest: " + e)
    } finally {
        threadIsRunning = false
    }
}

fun ArrayList<Byte>.equilsList(linkedList: LinkedList<Byte>): Boolean {
    if (linkedList.size != this.size) return false
    this.forEachIndexed { index, i ->
        if (i != linkedList[index]) return false
    }
    return true
}