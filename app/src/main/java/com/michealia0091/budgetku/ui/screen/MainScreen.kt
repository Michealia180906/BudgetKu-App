package com.michealia0091.budgetku.ui.screen

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.michealia0091.budgetku.R
import com.michealia0091.budgetku.database.KeuanganDb
import com.michealia0091.budgetku.model.Keuangan
import com.michealia0091.budgetku.navigation.Screen
import com.michealia0091.budgetku.ui.theme.BudgetKuTheme
import com.michealia0091.budgetku.util.SettingsDataStore
import com.michealia0091.budgetku.util.ViewModelFactory
import com.michealia0091.budgetku.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val db = KeuanganDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)
    val viewModel: MainViewModel = viewModel(factory = factory)

    val settingsDataStore = SettingsDataStore(context)
    val showList by settingsDataStore.layoutFlow.collectAsState(initial = true)

    val daftarKeuangan by viewModel.dataKeuangan.collectAsState()

    var keterangan by rememberSaveable { mutableStateOf("") }
    var nominal by rememberSaveable { mutableStateOf("") }
    var saldoManual by rememberSaveable { mutableStateOf("") }
    var jenis by rememberSaveable { mutableStateOf("Masuk") }

    val totalMasuk = daftarKeuangan
        .filter { it.jenis == "Masuk" }
        .sumOf { it.nominal }

    val totalKeluar = daftarKeuangan
        .filter { it.jenis == "Keluar" }
        .sumOf { it.nominal }

    val saldoAwal = saldoManual.toIntOrNull() ?: 0
    val saldo = saldoAwal + totalMasuk - totalKeluar

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("BudgetKu")
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(
                        onClick = {
                            scope.launch {
                                settingsDataStore.saveLayout(!showList)
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(
                                id = if (showList) {
                                    R.drawable.outline_grid_view_24
                                } else {
                                    R.drawable.baseline_view_list_24
                                }
                            ),
                            contentDescription = stringResource(
                                id = if (showList) {
                                    R.string.grid
                                } else {
                                    R.string.list
                                }
                            ),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    IconButton(
                        onClick = {
                            navController.navigate(Screen.About.route)
                        }
                    ) {
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

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            item {
                Text(
                    text = "Input Catatan Uang",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            item {
                OutlinedTextField(
                    value = keterangan,
                    onValueChange = { keterangan = it },
                    label = {
                        Text("Keterangan")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            item {
                OutlinedTextField(
                    value = nominal,
                    onValueChange = { nominal = it },
                    label = {
                        Text("Nominal")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            item {
                OutlinedTextField(
                    value = saldoManual,
                    onValueChange = { saldoManual = it },
                    label = {
                        Text("Saldo Manual")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            item {
                Text(
                    text = "Jenis Catatan",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = jenis == "Masuk",
                            onClick = {
                                jenis = "Masuk"
                            }
                        )
                        Text("Masuk")
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = jenis == "Keluar",
                            onClick = {
                                jenis = "Keluar"
                            }
                        )
                        Text("Keluar")
                    }
                }
            }

            item {
                Button(
                    onClick = {
                        val jumlah = nominal.toIntOrNull()
                        val saldoAwalInput = saldoManual.toIntOrNull() ?: 0

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
                            viewModel.insert(
                                keterangan = keterangan,
                                nominal = jumlah,
                                jenis = jenis,
                                saldoManual = saldoAwalInput
                            )

                            keterangan = ""
                            nominal = ""
                            jenis = "Masuk"

                            Toast.makeText(
                                context,
                                "Catatan berhasil disimpan",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("Simpan Catatan")
                }
            }

            item {
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
            }

            item {
                Text(
                    text = "Daftar Catatan",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (daftarKeuangan.isEmpty()) {
                item {
                    Text("Belum ada catatan uang.")
                }
            } else {
                if (showList) {
                    items(daftarKeuangan) { keuangan ->
                        KeuanganListItem(
                            keuangan = keuangan,
                            onClick = {
                                navController.navigate(
                                    Screen.FormUbah.withId(keuangan.id)
                                )
                            }
                        )
                    }
                } else {
                    items(daftarKeuangan.chunked(2)) { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            rowItems.forEach { keuangan ->
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    KeuanganGridItem(
                                        keuangan = keuangan,
                                        onClick = {
                                            navController.navigate(
                                                Screen.FormUbah.withId(keuangan.id)
                                            )
                                        }
                                    )
                                }
                            }

                            if (rowItems.size == 1) {
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {}
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun KeuanganListItem(
    keuangan: Keuangan,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = if (keuangan.jenis == "Masuk") {
                    "+ Rp${keuangan.nominal}"
                } else {
                    "- Rp${keuangan.nominal}"
                },
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = keuangan.keterangan,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(text = keuangan.jenis)
        }
    }
}

@Composable
fun KeuanganGridItem(
    keuangan: Keuangan,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = if (keuangan.jenis == "Masuk") {
                    "+ Rp${keuangan.nominal}"
                } else {
                    "- Rp${keuangan.nominal}"
                },
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = keuangan.keterangan,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )

            Text(text = keuangan.jenis)
        }
    }
}

@Preview(showBackground = true)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
fun MainScreenPreview() {
    BudgetKuTheme {
        MainScreen(rememberNavController())
    }
}