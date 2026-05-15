package com.michealia0091.budgetku.ui.screen

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.michealia0091.budgetku.database.KeuanganDb
import com.michealia0091.budgetku.ui.theme.BudgetKuTheme
import com.michealia0091.budgetku.util.ViewModelFactory
import com.michealia0091.budgetku.viewmodel.DetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    navController: NavHostController,
    id: Long
) {
    val context = LocalContext.current
    val db = KeuanganDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)
    val viewModel: DetailViewModel = viewModel(factory = factory)

    var keterangan by rememberSaveable { mutableStateOf("") }
    var nominal by rememberSaveable { mutableStateOf("") }
    var saldoManual by rememberSaveable { mutableStateOf("") }
    var jenis by rememberSaveable { mutableStateOf("Masuk") }
    var showDialog by rememberSaveable { mutableStateOf(false) }

    val isEditMode = id != 0L

    LaunchedEffect(id) {
        if (isEditMode) {
            val data = viewModel.getKeuangan(id)
            if (data != null) {
                keterangan = data.keterangan
                nominal = data.nominal.toString()
                saldoManual = data.saldoManual.toString()
                jenis = data.jenis
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (isEditMode) "Ubah Catatan" else "Tentang Aplikasi")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { innerPadding ->

        if (!isEditMode) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("BudgetKu", style = MaterialTheme.typography.headlineMedium)
                Text(
                    text = "Aplikasi sederhana untuk mencatat pemasukan, pengeluaran, dan menghitung saldo.",
                    modifier = Modifier.padding(top = 16.dp)
                )
                Text(
                    text = "Dibuat oleh Michealia",
                    modifier = Modifier.padding(top = 24.dp)
                )
                Text(
                    text = "© 2026 BudgetKu App",
                    modifier = Modifier.padding(top = 32.dp)
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
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

                Text("Jenis Catatan")

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
                        val saldoAwal = saldoManual.toIntOrNull() ?: 0

                        if (keterangan.isBlank() || nominal.isBlank()) {
                            Toast.makeText(
                                context,
                                "Keterangan dan nominal harus diisi!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else if (jumlah == null || jumlah <= 0) {
                            Toast.makeText(
                                context,
                                "Nominal harus berupa angka lebih dari 0!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            viewModel.update(
                                id = id,
                                keterangan = keterangan,
                                nominal = jumlah,
                                jenis = jenis,
                                saldoManual = saldoAwal
                            )
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Simpan Perubahan")
                }

                Button(
                    onClick = {
                        showDialog = true
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Hapus Catatan")
                }

                if (showDialog) {
                    DisplayAlertDialog(
                        onDismissRequest = {
                            showDialog = false
                        },
                        onConfirmation = {
                            viewModel.delete(id)
                            showDialog = false
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun DetailScreenPreview() {
    BudgetKuTheme {
        DetailScreen(
            navController = rememberNavController(),
            id = 1L
        )
    }
}