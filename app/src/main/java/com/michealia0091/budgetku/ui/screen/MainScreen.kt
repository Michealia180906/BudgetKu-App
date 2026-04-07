package com.michealia0091.budgetku.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.Alignment
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController


import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import com.michealia0091.budgetku.R
import com.michealia0091.budgetku.navigation.Screen
import com.michealia0091.budgetku.ui.theme.BudgetKuTheme

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {

    var nama by remember { mutableStateOf("") }
    var uangAwal by remember { mutableStateOf("") }
    var pengeluaran by remember { mutableStateOf("") }
    var kategori by remember { mutableStateOf("Makan") }

    var hasil by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(id = R.string.app_name))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),


                actions = {
                    IconButton(onClick = {
                        navController.navigate(Screen.About.route)
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = stringResource(R.string.tentang_aplikasi),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.koin),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentScale = ContentScale.Crop
            )

            Text(
                text = stringResource(id = R.string.judul),
                style = MaterialTheme.typography.titleLarge
            )

            OutlinedTextField(
                value = nama,
                onValueChange = { nama = it },
                label = { Text(stringResource(id = R.string.nama)) },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uangAwal,
                onValueChange = { uangAwal = it },
                label = { Text(stringResource(id = R.string.uang_awal)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = pengeluaran,
                onValueChange = { pengeluaran = it },
                label = { Text(stringResource(id = R.string.pengeluaran)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = stringResource(id = R.string.kategori),
                style = MaterialTheme.typography.titleMedium
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = kategori == "Makan",
                        onClick = { kategori = "Makan" }
                    )
                    Text(stringResource(id = R.string.makan))
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = kategori == "Transport",
                        onClick = { kategori = "Transport" }
                    )
                    Text(stringResource(id = R.string.transport))
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = kategori == "Lainnya",
                        onClick = { kategori = "Lainnya" }
                    )
                    Text(stringResource(id = R.string.lainnya))
                }
            }

            Button(
                onClick = {
                    if (nama.isEmpty() || uangAwal.isEmpty() || pengeluaran.isEmpty()) {
                        error = "Semua data harus diisi!"
                        hasil = ""
                    } else {
                        val uang = uangAwal.toIntOrNull()
                        val keluar = pengeluaran.toIntOrNull()

                        if (uang == null || keluar == null) {
                            error = "Masukkan angka yang valid!"
                            hasil = ""
                        } else {
                            val sisa = uang - keluar

                            val persen = if (uang == 0) 0f else sisa.toFloat() / uang * 100

                            val status = when {
                                uang == 0 && keluar == 0 -> "Tidak ada"
                                persen > 50 -> "Hemat"
                                persen >= 20 -> "Cukup"
                                else -> "Boros"
                            }

                            hasil = """
Nama: $nama
Sisa Uang: Rp$sisa
Status: $status
Kategori: $kategori
""".trimIndent()

                            error = ""
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(stringResource(id = R.string.hitung))
            }

            if (error.isNotEmpty()) {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error
                )
            }

            if (hasil.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Text(
                        text = hasil,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun PreviewMainScreen() {
    BudgetKuTheme {
        MainScreen(rememberNavController())
    }
}