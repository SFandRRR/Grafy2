package com.example.grafy2

import android.app.PendingIntent.getActivity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Config.DEBUG
import android.util.Log
import android.util.Log.DEBUG
import android.widget.*
import com.example.grafy2.R
import java.util.Queue


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
                    ConnectedTo+=i+1
                }
            }
        }

        fun SetData(value : String){
            Data=value;
        }
    }// End of class Vertex

    class Matrix(val NumberOfVertices : Int){
        var Verticies : Array<Vertex> = arrayOf(Vertex(NumberOfVertices))
        val NOV = NumberOfVertices

        fun CreateVerticies(){
            for( i in 0..NOV-2){
                Verticies = Verticies+ arrayOf(Vertex(NOV))
            }
        }
    }// End of class Matrix

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val Label_VertexNum : TextView = findViewById(R.id.TextView_Vertex)
        val Label_Path : TextView = findViewById(R.id.TextView_Path)

        val Button_ConnectVertex : Button = findViewById(R.id.Button_ConnectVertex)
        val Button_DisconnectVertex : Button = findViewById(R.id.Button_DisconnectVertex)
        val Button_AddData : Button = findViewById(R.id.Button_AddDataToVertex)

        val Button_CreateMatrix : Button = findViewById(R.id.Button_CreateMacierz)

        val Button_DFS : Button = findViewById(R.id.button_DFS)
        val Button_BFS : Button = findViewById(R.id.button_BFS)

        val Slider_Vertex : SeekBar = findViewById(R.id.SeekBar_Vertex)
        val Slider_Connector : SeekBar = findViewById(R.id.SeekBar_ConnectWith)

        val Slider_PFrom : SeekBar = findViewById(R.id.SeekBar_PathFrom)
        val Slider_PTo : SeekBar = findViewById(R.id.SeekBar_Pathto)

        var Slider_MSize : SeekBar = findViewById(R.id.SeekBar_MacierzSize)

        val Input_Data : EditText = findViewById(R.id.EditText_data)

        var SeekMSize = 4
        var MacierzSize = 4

        var ConnectWith = 1
        var NV = 1

        var SearchFrom = 0
        var SearchTo = 0

        var Macierz = Matrix(MacierzSize)
        Macierz.CreateVerticies()
        Toast.makeText(applicationContext, Macierz.Verticies.size.toString(), Toast.LENGTH_SHORT).show()

        Slider_Vertex.max=MacierzSize
        Slider_Connector.max=MacierzSize

        Slider_PFrom.max=MacierzSize
        Slider_PTo.max=MacierzSize

        Button_CreateMatrix.setOnClickListener {

            MacierzSize = SeekMSize
            Macierz = Matrix(MacierzSize)
            Macierz.CreateVerticies()
            Toast.makeText(applicationContext, Macierz.Verticies.size.toString(), Toast.LENGTH_SHORT).show()

            Slider_Vertex.max=MacierzSize
            Slider_Connector.max=MacierzSize

            Slider_PFrom.max=MacierzSize
            Slider_PTo.max=MacierzSize
        }

        fun DFS_Path(Vstart : Int, Vend : Int ){
            var Path : IntArray= IntArray(Macierz.Verticies.size)
            var Visited : BooleanArray= BooleanArray(Macierz.Verticies.size)

            var Stos = ArrayDeque<Int>()

            var CurrentVertex=0
            var NextVertex=0

            var found = false

            for(i in 0..Visited.size-1){
                Visited[i]=false
            }

            Visited[Vstart]=true
            Path[Vstart]=-1
            Stos.add(Vstart)

            while(Stos.isEmpty()==false){

                CurrentVertex=Stos.first()
                Stos.removeFirst()

                for(i in Stos){
                    Label_Path.text=Label_Path.text.toString()+i.toString()+" "
                }

                if(CurrentVertex==Vend){
                    Label_Path.text="Wynik: "
                    found=true
                    while(CurrentVertex>-1){
                        Label_Path.text=Label_Path.text.toString()+(CurrentVertex+1).toString()+" "
                        CurrentVertex=Path[CurrentVertex]
                    }
                    break
                }


                for(NV in Macierz.Verticies[CurrentVertex].ConnectedTo){
                    var NeV=0
                    if(NV!=0){
                        NeV=NV-1
                    }

                    if(Visited[NeV]==false){
                        Path[NeV]=CurrentVertex
                        Stos.add(NeV)
                        Visited[NeV]==true
                        for(i in Stos){
                            Label_Path.text=Label_Path.text.toString()+i.toString()+" "
                        }
                    }
                }
            }
            if(found==false) {
                Label_Path.text = Label_Path.text.toString() + "Brak ścieżki "
            }
        }


        fun BFS_Path(Vstart : Int, Vend : Int ){
            var Path : IntArray= IntArray(Macierz.Verticies.size)
            var Visited : BooleanArray = BooleanArray(Macierz.Verticies.size)

            var found = false

            var Queue = ArrayDeque<Int>()

            var CurrentVertex=0
            var NextVertex=0


            for(i in 0..Visited.size-1){
                Visited[i]=false
            }
            Visited[Vstart]=true
            Path[Vstart]=-1
            Queue.add(Vstart)


            while(Queue.isEmpty()==false){
                CurrentVertex=Queue.first()
                Queue.removeFirst()

                if(CurrentVertex==Vend){
                    Label_Path.text="Wynik: "
                    found=true
                    while(CurrentVertex>-1){
                        Label_Path.text=Label_Path.text.toString()+(CurrentVertex+1).toString()+" "
                        CurrentVertex=Path[CurrentVertex]
                    }
                    break
                }

                for(NV in Macierz.Verticies[CurrentVertex].ConnectedTo){
                    var NeV=NV-1
                    if(Visited[NeV]==false){
                        Path[NeV]=CurrentVertex
                        Queue.add(NeV)
                        Visited[NeV]==true
                    }
                }
            }
            if(found==false) {
                Label_Path.text = Label_Path.text.toString() + "Brak ścieżki "
            }
        }

        fun UpdateVertexInfo(){

            var str =""
            str+=NV.toString()+"\nPołączony z: "
            for(i in Macierz.Verticies[NV-1].ConnectedTo){
                str+=i.toString()+","
            }
            str+="\nWartość : "+Macierz.Verticies[NV-1].Data
            Label_VertexNum.text=str
        }
        UpdateVertexInfo()


        Button_DFS.setOnClickListener(){
            Label_Path.text=""
            if(SearchFrom!=SearchTo){
                DFS_Path(SearchFrom-1,SearchTo-1)
            }else{
                Label_Path.text="Ścieżka do samego siebie!"
            }
        }
        Button_BFS.setOnClickListener(){
            Label_Path.text=""
            if(SearchFrom!=SearchTo){
                BFS_Path(SearchFrom-1,SearchTo-1)
            }else{
                Label_Path.text="Ścieżka do samego siebie!"
            }
        }

        Button_ConnectVertex.setOnClickListener(){
            if(!(ConnectWith==NV)){
                Macierz.Verticies[NV-1].Connect(ConnectWith,1)
                UpdateVertexInfo()
            }else{
                Toast.makeText(applicationContext, "Nie można połączyć samego z sobą", Toast.LENGTH_SHORT).show()
            }
        }

        Button_DisconnectVertex.setOnClickListener(){
            if(!(ConnectWith==NV)){
                Macierz.Verticies[NV-1].Connect(ConnectWith,0)
                UpdateVertexInfo()
            }else{
                Toast.makeText(applicationContext, "Nie można połączyć samego z sobą", Toast.LENGTH_SHORT).show()
            }
        }

        Button_AddData.setOnClickListener(){
            Macierz.Verticies[NV-1].Data=Input_Data.text.toString()
            Toast.makeText(applicationContext, "Dodano!", Toast.LENGTH_SHORT).show()
            UpdateVertexInfo()
        }

        Slider_Vertex.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                NV = Slider_Vertex.progress
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

        Slider_PFrom.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                SearchFrom = progress
                Button_BFS.text="(BFS) Szukaj ścieżki od "+SearchFrom.toString()+" do "+SearchTo.toString()
                Button_DFS.text="(DFS) Szukaj ścieżki od "+SearchFrom.toString()+" do "+SearchTo.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })

        Slider_PTo.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                SearchTo = progress
                Button_BFS.text="(BFS) Szukaj ścieżki od "+SearchFrom.toString()+" do "+SearchTo.toString()
                Button_DFS.text="(DFS) Szukaj ścieżki od "+SearchFrom.toString()+" do "+SearchTo.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })

        Slider_MSize.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                SeekMSize = progress
                Button_CreateMatrix.text="Utwórz Macierz o wielkości "+SeekMSize.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })

    }
}