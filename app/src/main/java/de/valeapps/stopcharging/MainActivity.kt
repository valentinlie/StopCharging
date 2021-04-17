package de.valeapps.stopcharging

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import eu.chainfire.libsuperuser.Shell

const val DEFAULT_FILE = "/sys/class/power_supply/battery/charging_enabled"
const val XIAOMI_FILE = "/sys/class/power_supply/battery/input_suspend"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (!Shell.SU.available()) {
            Toast.makeText(this, "No Root enabled!", Toast.LENGTH_LONG).show()
            finishAffinity()
        }
        var cfInitialized = false

        val suShell: Shell.Interactive = Shell.Builder().setWantSTDERR(false).useSH().open()

        val buttonStop = findViewById<Button>(R.id.button_stop)
        buttonStop.setText("Stop Charging")
        val buttonStart = findViewById<Button>(R.id.button_start)
        buttonStart.setText("Start Charging")

        buttonStop.setOnClickListener {
            if (cfInitialized) {
                suShell.addCommand("echo 1 > " + XIAOMI_FILE)
            } else {
                suShell.addCommand("su")
                suShell.addCommand("mount -o rw,remount " + XIAOMI_FILE)
                suShell.addCommand("chmod u+w " + XIAOMI_FILE)
                suShell.addCommand("echo 1 > " + XIAOMI_FILE)
                cfInitialized = true
            }
            Toast.makeText(this, "Stopped charging..", Toast.LENGTH_SHORT).show()
        }
        buttonStart.setOnClickListener {
            if (cfInitialized) {
                suShell.addCommand("echo 0 > " + XIAOMI_FILE)
            } else {
                suShell.addCommand("su")
                suShell.addCommand("mount -o rw,remount " + XIAOMI_FILE)
                suShell.addCommand("chmod u+w " + XIAOMI_FILE)
                suShell.addCommand("echo 0 > " + XIAOMI_FILE)
                cfInitialized = true
            }
            Toast.makeText(this, "Started charging..", Toast.LENGTH_SHORT).show()
        }
    }
}
