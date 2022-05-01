package com.example.myoungji_walk_android

import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.ar.sceneform.rendering.ModelRenderable
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutionException

class ModelRender {
/*
    fun a() {
        //AR 이미지 초기화
        val goal = ModelRenderable.builder()
            .setSource(this, Uri.parse("check_point.glb"))
            .setIsFilamentGltf(true)
            .setAsyncLoadEnabled(true)
            .build()
        CompletableFuture.allOf(goal)
            .handle<Any?>
            { notUsed: Void?, throwable: Throwable? ->
                if (throwable != null) {
                    return@handle null
                }
                try {
                    checkPointRender = goal.get()
                } catch (e: InterruptedException) {
                    e.getStackTrace()
                } catch (e: ExecutionException) {
                    e.getStackTrace()
                }
                null
            }
    }

    fun b() {
        val goal2 = ModelRenderable.builder()
            .setSource(this, Uri.parse("arrow.glb"))
            .setIsFilamentGltf(true)
            .setAsyncLoadEnabled(true)
            .build()
        CompletableFuture.allOf(goal2)
            .handle<Any?> { notUsed: Void?, throwable: Throwable? ->
                if (throwable != null) {
                    return@handle null
                }
                try {
                    arrowRender = goal2.get()
                } catch (e: InterruptedException) {
                    e.stackTrace
                } catch (e: ExecutionException) {
                    e.stackTrace
                }
                null
            }
    }

 */
}