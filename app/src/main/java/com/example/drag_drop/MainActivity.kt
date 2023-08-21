package com.example.drag_drop

import android.content.ClipData
import android.content.ClipDescription
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import com.example.drag_drop.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding?.root
        setContentView(view)
        //
        binding?.mainLlTop?.setOnDragListener(dragListener)
        binding?.mainLlBottom?.setOnDragListener(dragListener)
        //
        binding?.mainDragView?.setOnLongClickListener{
            // >> This string will be used as the data
            // you want to transfer during the drag-and-drop operation.
            val clipText = "This is our ClipData text"

            // >> create a new Item of Clip Data
            val item = ClipData.Item(clipText)

            // >> MIME types are used to describe the type
            // of data being transferred in a standardized format
            val mimeTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)

            // >> create a new Clip, The ClipData class represents the data
            // to be transferred during a drag-and-drop operation.
            val data = ClipData(clipText, mimeTypes, item)

            // >> create Drag & Drop Builder
            val dragShadowBuilder = View.DragShadowBuilder(it)
            it.startDragAndDrop(data, dragShadowBuilder, it, 0)
            it.visibility = View.INVISIBLE
            true

        }
    }

    val dragListener = View.OnDragListener{ view, event ->
        when(event.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                event.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)
            }
            DragEvent.ACTION_DRAG_ENTERED -> {
                view.invalidate()
                true
            }
            DragEvent.ACTION_DRAG_LOCATION -> {
                true
            }
            DragEvent.ACTION_DRAG_EXITED -> {
                view.invalidate()
                true
            }
            DragEvent.ACTION_DROP -> {
                val item = event.clipData.getItemAt(0)
                val dragData = item.text
                Toast.makeText(this@MainActivity, dragData, Toast.LENGTH_SHORT).show()

                view.invalidate()

                val v = event.localState as View
                val owner = v.parent as ViewGroup
                owner.removeView(v)

                val destiniation = view as LinearLayout
                destiniation.addView(v)
                v.visibility = View.VISIBLE
                true
            }
            DragEvent.ACTION_DRAG_ENDED -> {
                view.invalidate()
                true
            }
            else-> false

        }

    }


    //
    override fun onDestroy() {
        super.onDestroy()

        binding = null
    }
}