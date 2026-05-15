package com.michealia0091.budgetku.ui.screen

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun DisplayAlertDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit
) {
    AlertDialog(
        text = {
            Text("Hapus catatan ini?")
        },
        confirmButton = {
            TextButton(onClick = { onConfirmation() }) {
                Text("Hapus")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismissRequest() }) {
                Text("Batal")
            }
        },
        onDismissRequest = {
            onDismissRequest()
        }
    )
}