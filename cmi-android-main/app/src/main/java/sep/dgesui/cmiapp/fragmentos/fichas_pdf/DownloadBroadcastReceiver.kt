package sep.dgesui.cmiapp.fragmentos.fichas_pdf

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Environment
import androidx.fragment.app.FragmentManager

class DownloadBroadcastReceiver : BroadcastReceiver() {

    companion object{
        lateinit var fileName : String
        lateinit var fragmentManager: FragmentManager
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action
        if(DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)){
            val archivo = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .listFiles { directory, file ->
                    directory.length() > 0 && file.matches(Regex("("+fileName+".*)"+"(\\.pdf)"))
                }?.first()
            val dialog = archivo?.let { PdfFichaIndicador(it) }
            dialog?.show(fragmentManager,"pdf_ficha_indicador")
        }
    }
}