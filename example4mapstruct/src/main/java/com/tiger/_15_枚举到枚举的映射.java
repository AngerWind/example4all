package com.tiger;

import org.mapstruct.Mapper;
import org.mapstruct.ValueMapping;
import org.mapstruct.factory.Mappers;

public class _15_枚举到枚举的映射 {

    public enum TrafficSignal {
        Off, Stop, Go
    }
    public enum RoadSign {
        Off, Halt, Move
    }
    @Mapper
    public interface TrafficSignalMapper {
        TrafficSignalMapper INSTANCE = Mappers.getMapper(TrafficSignalMapper.class);

        @ValueMapping(target = "Off", source = "Off")
        @ValueMapping(target = "Go", source = "Move")
        @ValueMapping(target = "Stop", source = "Halt")
        TrafficSignal toTrafficSignal(RoadSign source);
    }
}
