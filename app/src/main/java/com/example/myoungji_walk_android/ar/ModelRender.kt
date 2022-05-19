package com.example.myoungji_walk_android.ar


import android.net.Uri
import com.example.myoungji_walk_android.ar.NavigationActivity.Companion.getInstance
import com.google.ar.sceneform.rendering.ModelRenderable
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutionException

class ModelRender {
    lateinit var checkPointRender: ModelRenderable
    lateinit var arrowRender: ModelRenderable

    fun checkPointModel() {
        //AR 이미지 초기화
        val render = ModelRenderable.builder()
            .setSource(getInstance(), Uri.parse("straight.glb"))
            .setIsFilamentGltf(true)
            .setAsyncLoadEnabled(true)
            .build()
        CompletableFuture.allOf(render)
            .handle<Any?> { notUsed: Void?, throwable: Throwable? ->
                if (throwable != null) {
                    return@handle null
                }
                try {
                    checkPointRender = render.get()
                } catch (e: InterruptedException) {
                    e.getStackTrace()
                } catch (e: ExecutionException) {
                    e.getStackTrace()
                }
                null
            }
    }

    fun arrowModel() {
        val render = ModelRenderable.builder()
            .setSource(getInstance(), Uri.parse("arrow.glb"))
            .setIsFilamentGltf(true)
            .setAsyncLoadEnabled(true)
            .build()
        CompletableFuture.allOf(render)
            .handle<Any?> { notUsed: Void?, throwable: Throwable? ->
                if (throwable != null) {
                    return@handle null
                }
                try {
                    arrowRender = render.get()
                } catch (e: InterruptedException) {
                    e.stackTrace
                } catch (e: ExecutionException) {
                    e.stackTrace
                }
                null
            }
    }
}