package com.example.grafy1

import android.app.PendingIntent.getActivity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Config.DEBUG
import android.util.Log
import android.util.Log.DEBUG
import android.widget.*
import com.example.grafy2.R


class MainActivity : AppCompatActivity() {

    class Vertex(val NumOVer : Int) {
        val Connections: IntArray = IntArray(NumOVer)
        var ConnectedTo: IntArray = IntArray(0)
        var Data = ""

        fun Connect(to : Int, how : Int){
            if(to >= 1 && to<=Connections.size){
                if(how >= 0 && how<=1){
                    Connections[to-1]=how
                }
            }
            UpdateConnectedTo()
        }

        fun UpdateConnectedTo(){
            ConnectedTo = IntArray(0)
            for(i in 0..Connections.size-1){
                if(Connections[i]==1){
                    ConnectedTo.plus(i+1)
                }
            }
        }

        fun SetData(value : String){
            Data=value;
        }
    }// End of class Vertex

    class Matrix(val NumberOfVertices : Int){
        val Verticies : Array<Vertex> = emptyArray()

        fun CreateVerticies(){
            for( i in 0..Verticies.size-1){
                Verticies.plus(Vertex(NumberOfVertices))
            }
        }
    }// End of class Matrix

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val Label_VertexNum : TextView = findViewById(R.id.TextView_Vertex)

        val Button_ConnectVertex : Button = findViewById(R.id.Button_ConnectVertex)
        val Button_DisconnectVertex : Button = findViewById(R.id.Button_DisconnectVertex)
        val Button_AddData : Button = findViewById(R.id.Button_AddDataToVertex)

        val Slider_Vertex : SeekBar = findViewById(R.id.SeekBar_Vertex)
        val Slider_Connector : SeekBar = findViewById(R.id.SeekBar_ConnectWith)

        val Input_Data : EditText = findViewById(R.id.EditText_data)

        val MacierzSize = 4
        var ConnectWith = 1
        var NV = Slider_Vertex.progress

        val Macierz = Matrix(MacierzSize)
        Macierz.CreateVerticies()

        Slider_Vertex.max=MacierzSize
        Slider_Connector.max=MacierzSize

        fun UpdateVertexInfo(){

            Label_VertexNum.text=NV.toString()+"\nPołączony z: "
            for(i in Macierz.Verticies[NV-1].ConnectedTo){
                Label_VertexNum.text=i.toString()+","
            }
            Label_VertexNum.text="\nWartość : "+Macierz.Verticies[NV-1].Data
        }

        Button_ConnectVertex.setOnClickListener(){
            if(!(ConnectWith==NV)){
                Macierz.Verticies[NV-1].Connect(ConnectWith-1,1)
                UpdateVertexInfo()
            }else{
                Toast.makeText(applicationContext, "Nie można połączyć samego z sobą", Toast.LENGTH_SHORT).show()
            }
        }

        Button_DisconnectVertex.setOnClickListener(){
            if(!(ConnectWith==NV)){
                Macierz.Verticies[NV-1].Connect(ConnectWith-1,0)
                UpdateVertexInfo()
            }else{
                Toast.makeText(applicationContext, "Nie można połączyć samego z sobą", Toast.LENGTH_SHORT).show()
            }
        }

        Button_AddData.setOnClickListener(){
            Macierz.Verticies[NV-1].Data=Input_Data.text.toString()
            Toast.makeText(applicationContext, "Dodano!", Toast.LENGTH_SHORT).show()
        }

        Slider_Vertex.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                UpdateVertexInfo()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })

        Slider_Connector.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                ConnectWith = progress
                Button_ConnectVertex.text="Połącz z "+progress.toString()
                Button_DisconnectVertex.text="Rozłącz z "+progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })

    }
}