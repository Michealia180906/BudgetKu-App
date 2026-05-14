package com.michealia0091.budgetku.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.michealia0091.budgetku.navigation.Screen
import com.michealia0091.budgetku.ui.theme.BudgetKuTheme

data class CatatanUang(
    val keterangan: String,
    val nominal: Int,
    val jenis: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {

    var keterangan by rememberSaveable { mutableStateOf("") }
    var nominal by rememberSaveable { mutableStateOf("") }
    var saldoManual by rememberSaveable { mutableStateOf("") }
    var jenis by rememberSaveable { mutableStateOf("Masuk") }
    var error by rememberSaveable { mutableStateOf("") }

    val daftarCatatan = remember { mutableStateListOf<CatatanUang>() }

    val totalMasuk = daftarCatatan
        .filter { it.jenis == "Masuk" }
        .sumOf { it.nominal }

    val totalKeluar = daftarCatatan
        .filter { it.jenis == "Keluar" }
        .sumOf { it.nominal }

    val saldoAwal = saldoManual.toIntOrNull() ?: 0
    val saldo = saldoAwal + totalMasuk - totalKeluar

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("BudgetKu") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(
                        onClick = {
                            navController.navigate(Screen.About.route)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = "Tentang Aplikasi",
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

            Text(
                text = "Input Catatan Uang",
                style = MaterialTheme.typography.titleLarge
            )

            OutlinedTextField(
                value = keterangan,
                onValueChange = { keterangan = it },
                label = { Text("Keterangan") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = nominal,
                onValueChange = { nominal = it },
                label = { Text("Nominal") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = saldoManual,
                onValueChange = { saldoManual = it },
                label = { Text("Saldo Manual") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Text(
                text = "Jenis Catatan",
                style = MaterialTheme.typography.titleMedium
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = jenis == "Masuk",
                        onClick = { jenis = "Masuk" }
                    )
                    Text("Masuk")
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = jenis == "Keluar",
                        onClick = { jenis = "Keluar" }
                    )
                    Text("Keluar")
                }
            }

            Button(
                onClick = {
                    val jumlah = nominal.toIntOrNull()

                    if (keterangan.isBlank() || nominal.isBlank()) {
                        error = "Keterangan dan nominal harus diisi!"
                    } else if (jumlah == null || jumlah <= 0) {
                        error = "Nominal harus berupa angka lebih dari 0!"
                    } else {
                        daftarCatatan.add(
                            CatatanUang(
                                keterangan = keterangan,
                                nominal = jumlah,
                                jenis = jenis
                            )
                        )

                        keterangan = ""
                        nominal = ""
                        jenis = "Masuk"
                        error = ""
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Simpan Catatan")
            }

            if (error.isNotEmpty()) {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("Total Masuk: Rp$totalMasuk")
                    Text("Total Keluar: Rp$totalKeluar")
                    Text("Saldo: Rp$saldo")
                }
            }

            Text(
                text = "Daftar Catatan",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )

            if (daftarCatatan.isEmpty()) {
                Text("Belum ada catatan uang.")
            } else {
                daftarCatatan.forEach { catatan ->
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = if (catatan.jenis == "Masuk") {
                                    "+ Rp${catatan.nominal}"
                                } else {
                                    "- Rp${catatan.nominal}"
                                },
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(catatan.keterangan)
                            Text(catatan.jenis)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun MainScreenPreview() {
    BudgetKuTheme {
        MainScreen(rememberNavController())
    }
}