package com.example.nfcclone.ui.screens

import android.nfc.NfcAdapter
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.nfcclone.R
import com.example.nfcclone.nfc.NfcManager
import com.example.nfcclone.nfc.NfcTagData

@Composable
fun HomeScreen(nfcAdapter: NfcAdapter?) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var nfcSupported by remember { mutableStateOf(nfcAdapter != null) }

    if (!nfcSupported) {
        NfcNotSupportedScreen()
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            "NFC Clone",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        TabRow(
            selectedTabIndex = selectedTab,
            modifier = Modifier.fillMaxWidth()
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text(stringResource(R.string.read_nfc_tags)) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text(stringResource(R.string.emulate_nfc)) }
            )
        }

        when (selectedTab) {
            0 -> ReadNfcScreen(nfcAdapter!!)
            1 -> EmulateNfcScreen()
        }
    }
}

@Composable
fun NfcNotSupportedScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "NFC nicht unterstützt",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            "Dieses Gerät unterstützt NFC nicht.",
            modifier = Modifier.padding(top = 16.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun ReadNfcScreen(nfcAdapter: NfcAdapter) {
    var tagData by remember { mutableStateOf<NfcTagData?>(null) }
    var statusMessage by remember { mutableStateOf("Bereit zum Lesen...") }
    var isReading by remember { mutableStateOf(true) }

    LaunchedEffect(isReading) {
        if (isReading) {
            statusMessage = "Halte ein NFC-Tag an dein Gerät..."
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { isReading = !isReading },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isReading) "Stopp" else "Start")
        }

        Text(
            statusMessage,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyMedium
        )

        if (tagData != null) {
            TagDataDisplay(tagData!!)
        } else {
            Text(
                stringResource(R.string.no_data),
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun TagDataDisplay(tagData: NfcTagData) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            DataRow(stringResource(R.string.id), tagData.id)
            DataRow(stringResource(R.string.type), tagData.type)
            DataRow(stringResource(R.string.technology), tagData.technologies.joinToString(", "))

            tagData.ndefMessage?.let {
                DataRow(stringResource(R.string.ndef_message), it)
            }
        }
    }
}

@Composable
fun DataRow(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            value,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun EmulateNfcScreen() {
    var isEmulating by remember { mutableStateOf(false) }
    var emulationData by remember { mutableStateOf("") }
    var statusMessage by remember { mutableStateOf("Emulation inaktiv") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Host Card Emulation (HCE)",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp)
        )

        Button(
            onClick = { isEmulating = !isEmulating },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isEmulating)
                    MaterialTheme.colorScheme.error
                else
                    MaterialTheme.colorScheme.primary
            )
        ) {
            Text(if (isEmulating) stringResource(R.string.emulation_enabled) else "Emulation starten")
        }

        Text(
            statusMessage,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyMedium
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    "Emulationsdetails",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    "Status: ${if (isEmulating) "Aktiv" else "Inaktiv"}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
