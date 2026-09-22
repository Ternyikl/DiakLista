package com.example.diaklista

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.diaklista.ui.theme.DiakListaTheme

data class Student(
    val id: Int,
    val name: String,
    val className: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme{
                StudentListScreen()
            }
        }
    }
}


@Composable
fun StudentListScreen()
{
    //A NÉV MEZŐ AKTUÁLIS TARTALMA
    var name: String by remember {
        mutableStateOf("")
    }

    //Az osztály MEZŐ AKTUÁLIS TARTALMA
    var className: String by remember {
        mutableStateOf("")
    }

    //ezzel generáljuk az egyedi azonosított a listában
    var nextId: Int by remember {
        mutableStateOf(1)
    }

    //mutableStateListOf változó -> Compose figyeli az értékváltozást
    //Ha hozzáadunk vagy törlünk egy diákot akkor auromatikusan frissül az oszlop
    val students = remember {
        mutableStateListOf<Student>()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(36.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ){
        Text(text = "DiákLista",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold)

        OutlinedTextField(
            value = name,
            onValueChange = { newValue ->
                name = newValue
            },
            label = {
                Text("Név")
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = className,
            onValueChange = { newValue ->
                className = newValue
            },
            label = {
                Text("Osztály")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                if (name.isNotBlank() && className.isNotBlank()){
                    val newStudent = Student(
                        id = nextId,
                        name = name,
                        className = className
                    )
                    students.add(newStudent)

                    nextId++;

                    name = "";
                    className = "";
                }
                //else alert ha üres a mező
            },

            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Diák hozzáadása")
        }

        Text(
            text = "Diákok száma: ${students.size}",
            style = MaterialTheme.typography.titleMedium
        )

        if(students.isEmpty())
        {
            Text(
                text = "Még nincs diák a listában",
                style = MaterialTheme.typography.bodyLarge
            )
        } else{
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = students,
                    key = {student -> student.id}
                ){
                    student ->
                    StudentCard(
                        student = student,
                        onDelete = {
                            students.remove(student)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun StudentCard(student: Student, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp)
        ){
            //TODO("baL OLDALOM MONOGRAM AVATAR")
            //Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = student.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = student.className,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            IconButton(
                onClick = onDelete
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id=R.drawable.delete_icon),
                    contentDescription = "Diák törlése"
                )
            }
        }
    }
}