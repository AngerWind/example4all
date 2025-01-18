package com.gitlab.techschool.pcbook.service;

import com.gitlab.techschool.pcbook.pb.*;
import com.gitlab.techschool.pcbook.service.exception.AlreadyExistsException;
import com.gitlab.techschool.pcbook.service.image.ImageStore;
import com.gitlab.techschool.pcbook.service.laptop.LaptopStore;
import com.gitlab.techschool.pcbook.service.laptop.LaptopStream;
import com.gitlab.techschool.pcbook.service.rate.Rating;
import com.gitlab.techschool.pcbook.service.rate.RatingStore;
import com.google.protobuf.ByteString;
import io.grpc.Context;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * @author Tiger.Shen
 * @version 1.0
 * @date 2024/10/18
 * @description
 */
public class LaptopService extends LaptopServiceGrpc.LaptopServiceImplBase {

    private static final Logger logger = Logger.getLogger(LaptopService.class.getName());

    private final LaptopStore laptopStore;
    private final ImageStore imageStore;
    private final RatingStore ratingStore;

    public LaptopService(LaptopStore laptopStore, ImageStore imageStore, RatingStore ratingStore) {
        this.laptopStore = laptopStore;
        this.imageStore = imageStore;
        this.ratingStore = ratingStore;
    }

    @Override
    public void createLaptop(CreateLaptopRequest request, StreamObserver<CreateLaptopResponse> responseObserver) {
        Laptop laptop = request.getLaptop();
        String id = laptop.getId();

        logger.info("got a create laptop request with ID: " + id);

        if (id.isEmpty()) {
            id = UUID.randomUUID().toString();
        }

        if (!isValidUUID(id)) {

            // 不管在服务器的responseObserver.onError()中设置什么异常
            // 客户端抛出的始终是StatusRuntimeException
            // 如果onError()中的异常是StatusRuntimeException或者StatusException, 那么客户端接收到的StatusRuntimeException的code与抛出的相同
            // 如果是其他异常, 那么客户端接收到的StatusRuntimeException的code为UNKNOWN

            responseObserver.onError(
                    // 通过asException()来创建一个StatusException
                    // 通过asRuntimeException()来创建一个StatusRuntimeException
                    Status.INVALID_ARGUMENT
                            .withDescription("id is not a valid uuid")
                            .asException());
            return;
        }

        // 深拷贝
        Laptop other = laptop.toBuilder().setId(id).build();

        // save
        try {
            laptopStore.save(other);
        } catch (AlreadyExistsException e) {

            responseObserver.onError(
                    Status.ALREADY_EXISTS
                            .withDescription(e.getMessage())
                            .asRuntimeException());
            return;
        } catch (Exception e) {
            responseObserver.onError(
                    Status.INTERNAL
                         .withDescription(e.getMessage())
                         .asRuntimeException());
            return;
        }

        // 模拟非常耗时的操作, 导致处理请求超时
        // try {
        //     TimeUnit.SECONDS.sleep(5);
        // } catch (InterruptedException e) {
        //     throw new RuntimeException(e);
        // }

        // blockingStub.withDeadlineAfter(5, TimeUnit.SECONDS).createLaptop(request1);
        // 如果客户端在通过上面代码设置了请求超时的时间, 那么时间到了的时候, Context.current().isCancelled()为true
        // 表示服务器处理请求超时了, 没有必要继续处理请求了
        if (Context.current().isCancelled()) {
            logger.info("request is cancelled");
            // todo 是否要调用onError, 还是直接return回去
            responseObserver.onError(
                    Status.CANCELLED
                         .withDescription("request is cancelled")
                         .asRuntimeException()
            );
            return;
        }

        // 响应客户端
        CreateLaptopResponse response = CreateLaptopResponse.newBuilder()
               .setId(other.getId())
               .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }


    @Override
    public void searchLaptop(SearchLaptopRequest request, StreamObserver<SearchLaptopResponse> responseObserver) {
        Filter filter = request.getFilter();
        logger.info("got a search-laptop request with filter:\n" + filter);

        laptopStore.search(Context.current(), filter, new LaptopStream() {
            @Override
            public void send(Laptop laptop) {
                logger.info("found laptop with ID: " + laptop.getId());

                // 只要有符合的laptop, 那么就通过onNext返回给客户端
                SearchLaptopResponse response = SearchLaptopResponse.newBuilder().setLaptop(laptop).build();
                responseObserver.onNext(response);
            }
        });

        // 告诉客户端完成请求了, 客户端的hasNext会返回false
        responseObserver.onCompleted();

        // 如果这里调用了responseObserver.onError()而不是responseObserver.onCompleted();
        // 那么在客户端的hasNext就会抛出一个StatusRuntimeException
        // responseObserver.onError(new RuntimeException("test"));

        logger.info("search laptop completed");
    }

    /**
     * 要处理客户端推流, 那么必须返回一个StreamObserver, 并且实现它的三个方法
     */
    @Override
    public StreamObserver<UploadImageRequest> uploadImage(StreamObserver<UploadImageResponse> responseObserver) {
        // 这个StreamObserver用于处理源源不断的请求
        return new StreamObserver<UploadImageRequest>() {

            // 用于限制能够上传的图片的总大小, 为1mb
            // private static final int maxImageSize = 1 << 20; // 1 megabyte
            private static final int maxImageSize = 1 << 10; // 1 kb

            // 保存当前请求接受到的laptop id
            private String laptopID;
            // 保存当前请求接受到的 imageType
            private String imageType;

            // 用于保存接受到的所有的chunk_data
            private ByteArrayOutputStream imageData;

            /**
             * 一旦客户端调用了requestObserver.onNext()向服务器推流, 就会执行这个方法
             */
            @Override
            public void onNext(UploadImageRequest request) {
                // 因为data是oneof, 接受到的要么是info, 要么是chunk_data
                // 这里判断接收到的是info还是chunk_data
                if (request.getDataCase() == UploadImageRequest.DataCase.INFO) {

                    // 获取接收到的info
                    ImageInfo info = request.getInfo();
                    logger.info("receive image info:\n" + info);

                    laptopID = info.getLaptopId();
                    imageType = info.getImageType();
                    imageData = new ByteArrayOutputStream();

                    // 判断关联的laptop是否存在
                    Laptop found = laptopStore.findById(laptopID);
                    if (found == null) {
                        responseObserver.onError(
                                Status.NOT_FOUND
                                        .withDescription("laptop ID doesn't exist")
                                        .asRuntimeException()
                        );
                        return;
                    }
                    return;
                }

                // 这里判断是否接受到的是 chunk_data
                if (request.getDataCase() == UploadImageRequest.DataCase.CHUNK_DATA) {
                    ByteString chunkData = request.getChunkData();
                    logger.info("receive image chunk with size: " + chunkData.size());

                    // 接受chunk_data之前, 必须先发送info过来
                    if (imageData == null) {
                        logger.info("image info wasn't sent before");
                        responseObserver.onError(
                                Status.INVALID_ARGUMENT
                                        .withDescription("image info wasn't sent before")
                                        .asRuntimeException()
                        );
                        return;
                    }

                    // 计算接收到的chunk是否超过限制的大小
                    int size = imageData.size() + chunkData.size();
                    if (size > maxImageSize) {
                        logger.info("image is too large: " + size);
                        responseObserver.onError(
                                Status.INVALID_ARGUMENT
                                        .withDescription("image is too large: " + size)
                                        .asRuntimeException()
                        );
                        return;
                    }

                    // 写入到buffer中
                    try {
                        chunkData.writeTo(imageData);
                    } catch (IOException e) {
                        responseObserver.onError(
                                Status.INTERNAL
                                        .withDescription("cannot write chunk data: " + e.getMessage())
                                        .asRuntimeException()
                        );
                        return;
                    }
                }
            }

            /**
             * 当客户端调用了requestObserver.onError(), 那么会调用这个方法, 并接受到一个StatusRuntimeException, code为CANCELLED
             */
            @Override
            public void onError(Throwable t) {
                logger.warning(t.getMessage());
            }

            /**
             * 一旦客户端调用了requestObserver.onCompleted(), 就会执行这个方法,
             * 表示客户端推流完毕
             */
            @Override
            public void onCompleted() {
                String imageID = "";
                int imageSize = imageData.size();

                try {
                    imageID = imageStore.save(laptopID, imageType, imageData);
                } catch (IOException e) {
                    responseObserver.onError(
                            Status.INTERNAL
                                    .withDescription("cannot save image to the store: " + e.getMessage())
                                    .asRuntimeException()
                    );
                    return;
                }

                // 返回生成的image id, 以及接收到的chunk的总大小
                UploadImageResponse response = UploadImageResponse.newBuilder()
                        .setId(imageID)
                        .setSize(imageSize)
                        .build();

                // 响应客户端
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public StreamObserver<RateLaptopRequest> rateLaptop(StreamObserver<RateLaptopResponse> responseObserver) {
        return new StreamObserver<RateLaptopRequest>() {

            /**
             * 一旦客户端调用了requestObserver.onNext()向服务器推流, 就会执行这个方法来处理推流
             */
            @Override
            public void onNext(RateLaptopRequest request) {
                String laptopId = request.getLaptopId();
                double score = request.getScore();

                logger.info("received rate-laptop request: id = " + laptopId + ", score = " + score);

                Laptop found = laptopStore.findById(laptopId);
                if (found == null) {
                    responseObserver.onError(
                            Status.NOT_FOUND
                                    .withDescription("laptop ID doesn't exist")
                                    .asRuntimeException()
                    );
                    return;
                }

                Rating rating = ratingStore.add(laptopId, score);
                RateLaptopResponse response = RateLaptopResponse.newBuilder()
                        .setLaptopId(laptopId)
                        .setRatedCount(rating.getCount())
                        .setAverageScore(rating.getSum() / rating.getCount())
                        .build();

                // 响应客户端的推流
                responseObserver.onNext(response);
            }

            /**
             * 当客户端调用了requestObserver.onError(), 那么会调用这个方法, 并接受到一个StatusRuntimeException, code为CANCELLED
             *
             */
            @Override
            public void onError(Throwable t) {
                // 在这个方法中, 不需要调用responseObserver.onError()或者responseObserver.onCompleted()来说明服务器推流完毕
                logger.warning(t.getMessage());
            }

            /**
             * 一旦客户端调用了requestObserver.onCompleted(), 就会执行这个方法, 表示客户端推流完成
             */
            @Override
            public void onCompleted() {
                // 在这个方法中, 需要调用responseObserver.onCompleted()来说明服务器推流完毕
                responseObserver.onCompleted();
            }
        };
    }

    public boolean isValidUUID(String uuid) {
        try {
            UUID.fromString(uuid);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

}
