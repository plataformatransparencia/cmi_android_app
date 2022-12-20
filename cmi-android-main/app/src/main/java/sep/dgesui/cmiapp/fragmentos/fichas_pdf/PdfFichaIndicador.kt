package sep.dgesui.cmiapp.fragmentos.fichas_pdf

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.github.barteksc.pdfviewer.PDFView
import sep.dgesui.cmiapp.R
import java.io.File

class PdfFichaIndicador(private val file:File) : DialogFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pdfView = view.findViewById<PDFView>(R.id.pdfView)
        pdfView.fromFile(file).load()

        view.findViewById<ImageView>(R.id.imageView4).setOnClickListener {
            dialog?.dismiss()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pdf_ficha_indicador,container,false)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
    }
}