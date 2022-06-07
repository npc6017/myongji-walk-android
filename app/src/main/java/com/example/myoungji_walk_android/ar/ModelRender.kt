package com.example.myoungji_walk_android.ar

import android.net.Uri
import com.example.myoungji_walk_android.ar.NavigationActivity.Companion.getInstance
import com.google.ar.sceneform.rendering.ModelRenderable
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutionException

class ModelRender {
    lateinit var straightRender: ModelRenderable
    lateinit var leftRender: ModelRenderable
    lateinit var rightRender: ModelRenderable
    lateinit var arrowRender: ModelRenderable
    lateinit var finishRender: ModelRenderable

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

    fun straightModel() {
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
                    straightRender = render.get()
                } catch (e: InterruptedException) {
                    e.getStackTrace()
                } catch (e: ExecutionException) {
                    e.getStackTrace()
                }
                null
            }
    }

    fun leftModel() {
        val render = ModelRenderable.builder()
            .setSource(getInstance(), Uri.parse("left.glb"))
            .setIsFilamentGltf(true)
            .setAsyncLoadEnabled(true)
            .build()
        CompletableFuture.allOf(render)
            .handle<Any?> { notUsed: Void?, throwable: Throwable? ->
                if (throwable != null) {
                    return@handle null
                }
                try {
                    leftRender = render.get()
                } catch (e: InterruptedException) {
                    e.stackTrace
                } catch (e: ExecutionException) {
                    e.stackTrace
                }
                null
            }
    }

    fun rightModel() {
        val render = ModelRenderable.builder()
            .setSource(getInstance(), Uri.parse("right.glb"))
            .setIsFilamentGltf(true)
            .setAsyncLoadEnabled(true)
            .build()
        CompletableFuture.allOf(render)
            .handle<Any?> { notUsed: Void?, throwable: Throwable? ->
                if (throwable != null) {
                    return@handle null
                }
                try {
                    rightRender = render.get()
                } catch (e: InterruptedException) {
                    e.stackTrace
                } catch (e: ExecutionException) {
                    e.stackTrace
                }
                null
            }
    }

    fun finishModel() {
        val render = ModelRenderable.builder()
            .setSource(getInstance(), Uri.parse("right.glb"))
            .setIsFilamentGltf(true)
            .setAsyncLoadEnabled(true)
            .build()
        CompletableFuture.allOf(render)
            .handle<Any?> { notUsed: Void?, throwable: Throwable? ->
                if (throwable != null) {
                    return@handle null
                }
                try {
                    finishRender = render.get()
                } catch (e: InterruptedException) {
                    e.stackTrace
                } catch (e: ExecutionException) {
                    e.stackTrace
                }
                null
            }
    }
}
