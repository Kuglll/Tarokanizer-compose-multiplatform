package di

import org.koin.core.context.startKoin

// Called from iOS
fun initKoin() {
    startKoin {
        modules(getSharedModules())
    }
}
