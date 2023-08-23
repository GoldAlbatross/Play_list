package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.Logger
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val toolsModule = module {
    singleOf(::Logger)
}